package net.seesaa.androidzaurus.android.sqltest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SQLiteTest extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button button = (Button) findViewById(R.id.Button01);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new SqliteTestTask().execute("a");
			}

		});
	}

	private class SqliteTestTask 
	extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			Debug.startMethodTracing("/data/anr/sqlite.trace");
			try {
				SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/anr/skk_dict.db", null,
						SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
				Cursor cr = db.query("dictionary", new String[] {"key", "value"},
						"key like \'a%\'", null, // new String[] { params[0] },
						null, null, null);
				if (cr.getCount() > 0) {
					cr.moveToFirst();
					int i = cr.getColumnIndex("value");
					String cs = cr.getString(i);
					i = cr.getCount();
					cr.close();
					db.close();
					Debug.stopMethodTracing();
					Log.d("SQLiteTest", i + " records: " + cs);
					return cs;
				}
				cr.close();
				db.close();
				Debug.stopMethodTracing();
				Log.d("SQLiteTest", "not found.");
				return null;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
