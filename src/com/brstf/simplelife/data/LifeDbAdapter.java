package com.brstf.simplelife.data;

import java.util.ArrayList;

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
	private final static String DATABASE_ROW = "(_id INTEGER PRIMARY "
			+ "KEY AUTOINCREMENT, life INTEGER NOT NULL)";
	private final static String DATABASE_CREATE_P1 = "CREATE TABLE IF NOT EXISTS "
			+ P1_TABLE + " " + DATABASE_ROW;
	private final static String DATABASE_CREATE_P2 = "CREATE TABLE IF NOT EXISTS "
			+ P2_TABLE + " " + DATABASE_ROW;

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
		for (Integer i : entries) {
			values.put("life", i);
			mDb.insert(table, null, values);
		}
	}

	/**
	 * Gets the name of the table for player 1.
	 * 
	 * @return Name of the database table for player 1
	 */
	public String getP1Table() {
		return P1_TABLE;
	}

	/**
	 * Gets the name of the table for player 2.
	 * 
	 * @return Name of the database table for player 2
	 */
	public String getP2Table() {
		return P2_TABLE;
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
}
