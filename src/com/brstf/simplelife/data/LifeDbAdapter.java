package com.brstf.simplelife.data;

import java.util.ArrayList;

import com.brstf.simplelife.controls.LifeController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LifeDbAdapter {
	private final static String DATABASE_NAME = "simplelife";
	private final static String P1_TABLE = "simplelife_p1";
	private final static String P2_TABLE = "simplelife_p2";
	private final static String P1_STAT_TABLE = "simplelife_stat_p1";
	private final static String P2_STAT_TABLE = "simplelife_stat_p2";
	private final static String DATABASE_ROW = "(_id INTEGER PRIMARY "
			+ "KEY AUTOINCREMENT, life INTEGER NOT NULL)";
	private final static String STAT_DATABASE_ROW = "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "total_loss INTEGER NOT NULL, total_gain INTEGER NOT NULL, avg_total REAL NOT "
			+ " NULL, avg_inc REAL NOT NULL, avg_dec REAL NOT NULL, avg_mod REAL NOT NULL, "
			+ "total_poison INTEGER NOT NULL, total_mod INTEGER NOT NULL, num_mod INTEGER "
			+ "NOT NULL)";
	private final static String DATABASE_CREATE_P1 = "CREATE TABLE IF NOT EXISTS "
			+ P1_TABLE + " " + DATABASE_ROW;
	private final static String DATABASE_CREATE_P2 = "CREATE TABLE IF NOT EXISTS "
			+ P2_TABLE + " " + DATABASE_ROW;
	private final static String DATABASE_CREATE_STATS_P1 = "CREATE TABLE IF NOT EXISTS "
			+ P1_STAT_TABLE + " " + STAT_DATABASE_ROW;
	private final static String DATABASE_CREATE_STATS_P2 = "CREATE TABLE IF NOT EXISTS "
			+ P2_STAT_TABLE + " " + STAT_DATABASE_ROW;

	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase mDb = null;
	private DatabaseHelper mDbHelper;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_P1);
			db.execSQL(DATABASE_CREATE_P2);

			db.execSQL(DATABASE_CREATE_STATS_P1);
			db.execSQL(DATABASE_CREATE_STATS_P2);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			if (!doesTableExist(P1_TABLE, db) || !doesTableExist(P2_TABLE, db)) {
				onCreate(db);
			}
		}

		private boolean doesTableExist(String tableName, SQLiteDatabase db) {
			Cursor cursor = db.rawQuery(
					"select DISTINCT tbl_name FROM sqlite_master where tbl_name = '"
							+ tableName + "'", null);

			if (cursor != null) {
				if (cursor.getCount() > 0) {
					cursor.close();
					return true;
				}
				cursor.close();
			}
			return false;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			resetDb(db);
		}

		/**
		 * Clears and resets the tables in the database.
		 * 
		 * @param db
		 *            Database to reset
		 */
		public void resetDb(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + P1_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + P2_TABLE);

			onCreate(db);
		}
	}

	public LifeDbAdapter(Context context) {
		this.mDbHelper = new DatabaseHelper(context);
	}

	public void open() {
		mDb = mDbHelper.getWritableDatabase();
		mDbHelper.onCreate(mDb);
	}

	public void beginTransation() {
		mDb.beginTransaction();
	}

	public void setTransactionSuccessful() {
		mDb.setTransactionSuccessful();
	}

	public SQLiteDatabase getDatabase() {
		return mDb;
	}

	public void close() {
		mDbHelper.close();
	}

	public void endTransaction() {
		mDb.endTransaction();
	}

	/**
	 * Add the given integer into the specified table.
	 * 
	 * @param table
	 *            Name of the table to add the entry into
	 * @param entry
	 *            Integer to add into the table
	 */
	public long addEntry(String table, int entry) {
		ContentValues values = new ContentValues();
		values.put("life", entry);
		return mDb.insert(table, null, values);
	}

	/**
	 * Add every integer from the given list into the specified table.
	 * 
	 * @param table
	 *            Name of the table to add the entries into
	 * @param entries
	 *            Integers to add into the table
	 */
	public void addEntries(String table, ArrayList<Integer> entries) {
		ContentValues values = new ContentValues();
		mDb.beginTransaction();
		try {
			for (Integer i : entries) {
				values.put("life", i);
				mDb.insert(table, null, values);
			}
			mDb.setTransactionSuccessful();
		} finally {
			mDb.endTransaction();
		}
	}

	/**
	 * Gets the name of the table for player 1.
	 * 
	 * @return Name of the database table for player 1
	 */
	public static String getP1Table() {
		return P1_TABLE;
	}

	/**
	 * Gets the name of the table for player 2.
	 * 
	 * @return Name of the database table for player 2
	 */
	public static String getP2Table() {
		return P2_TABLE;
	}

	/**
	 * Gets the name of the table for player 1 stats.
	 * 
	 * @return Name of the database table for player 1 stats
	 */
	public static String getP1StatsTable() {
		return P1_STAT_TABLE;
	}

	/**
	 * Gets the name of the table for player 2 stats.
	 * 
	 * @return Name of the database table for player 2 stats
	 */
	public static String getP2StatsTable() {
		return P2_STAT_TABLE;
	}

	public void clear() {
		mDbHelper.resetDb(mDb);
	}

	/**
	 * Gets a list of every integer in the given table
	 * 
	 * @param table
	 *            Table to get all the entries from
	 * @return List of all integers in the given table
	 */
	public ArrayList<Integer> getAllFrom(String table) {
		Cursor c = mDb.rawQuery("select * from " + table, null);
		ArrayList<Integer> entries = new ArrayList<Integer>();
		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {
				entries.add(c.getInt(c.getColumnIndex("life")));
				c.moveToNext();
			}
		}
		c.close();
		return entries;
	}

	public int getRowCount(String table) {
		Cursor c = mDb.rawQuery("select count(*) from " + table, null);
		c.moveToFirst();
		int count = c.getInt(0);
		c.close();
		return count;
	}

	/**
	 * Add poison and life history values from the given life controller to the
	 * table with the given name.
	 * 
	 * @param table
	 *            Name of the table to save the history into
	 * @param lc
	 *            LifeController to get the values to fill the table with from
	 */
	public void addLife(String table, LifeController lc) {
		ArrayList<Integer> entries = new ArrayList<Integer>();
		entries.add(lc.getCurrentPoison());
		entries.addAll(lc.getHistory());
		Log.d("DEBUG", "Saving " + String.valueOf(entries.size()) + " entries");
		addEntries(table, entries);
	}

	/**
	 * Restores the entries saved in the given table to the given life
	 * controller.
	 * 
	 * @param table
	 *            Name of the table to retrieve entries from
	 * @param lc
	 *            LifeController to restore values into
	 */
	public void restoreLife(String table, LifeController lc) {
		ArrayList<Integer> entries = getAllFrom(table);
		if (entries.size() > 0) {
			lc.setPoison(entries.get(0));
			entries.remove(0);
			lc.setHistory(entries);
		}
	}

	/**
	 * Compute relevant stats from the given LifeController and store the stats
	 * in the names stat database.
	 * 
	 * @param lc
	 *            LifeController to compute statistics from
	 * @param table
	 *            Name of the table to store the statistics in
	 */
	public void addStatsFromTo(LifeController lc, String table) {
		// Compute total decrement, increment, poison, mod, number of mods
		int num_inc = 0;
		int num_dec = 0;
		int total_mod = 0;
		int total_inc = 0;
		int total_dec = 0;
		int total = lc.getHistory().get(0);
		for (int i = 1; i < lc.getHistory().size(); ++i) {
			int mod = lc.getHistory().get(i) - lc.getHistory().get(i - 1);
			if (mod > 0) {
				++num_inc;
				total_inc += mod;
				total_mod += mod;
			} else {
				++num_dec;
				total_dec -= mod;
				total_mod -= mod;
			}
			total += lc.getHistory().get(i);
		}

		int num_mods = lc.getHistory().size() - 1;
		int total_poison = lc.getCurrentPoison();

		// Compute average life total, average increment size, average
		// decrement size, average mod
		float avg_total = ((float) total) / ((float) lc.getHistory().size());
		float avg_inc = ((float) total_inc) / ((float) num_inc);
		float avg_dec = ((float) total_dec) / ((float) num_dec);
		float avg_mod = ((float) total_mod) / ((float) num_mods);

		// Put everything into a ContentValues and write it to the db
		ContentValues values = new ContentValues();
		mDb.beginTransaction();
		values.put("total_loss", total_dec);
		values.put("total_gain", total_inc);
		values.put("avg_total", avg_total);
		values.put("avg_inc", avg_inc);
		values.put("avg_dec", avg_dec);
		values.put("avg_mod", avg_mod);
		values.put("total_poison", total_poison);
		values.put("total_mod", total_mod);
		values.put("num_mod", num_mods);
		try {
			mDb.insert(table, null, values);
			mDb.setTransactionSuccessful();
		} finally {
			mDb.endTransaction();
		}
	}
}
