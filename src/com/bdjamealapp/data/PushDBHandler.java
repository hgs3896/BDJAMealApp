package com.bdjamealapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class PushDBHandler {
    private PushDBManager helper;
    private SQLiteDatabase db;

    public static final String TABLE = "pushLog";
    public static final String ID = "_id";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String MSG = "msg";

    private PushDBHandler(final Context ct) {
        helper = new PushDBManager(ct);
        db = helper.getWritableDatabase();
    }

    public static PushDBHandler open(final Context ct) throws SQLException {
        PushDBHandler handler = new PushDBHandler(ct);
        return handler;
    }

    public void close() {
        helper.close();
    }

    public long insert(final String type, final String title, final String msg) {
        ContentValues values = new ContentValues();
        values.put(TYPE, type);
        values.put(TITLE, title);
        values.put(MSG, msg);
        return db.insert(TABLE, null, values);
    }

    public Cursor select(final int id) throws SQLException {
        Cursor cursor = db.query(true, TABLE, new String[]{ID, TYPE, TITLE, MSG}, "_id=" + id, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor selectAll() throws SQLException {
        // Cursor cursor = db.query(true, TABLE, new String[]{ID, TYPE, TITLE, MSG}, null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public int getCount() throws SQLException {
        int size = 0;
        Cursor cursor = selectAll();
        size = cursor.getCount();
        cursor.close();
        return size;
    }

    private class PushDBManager extends SQLiteOpenHelper {

        static final int DB_VERSION = 1;

        public PushDBManager(final Context ct) {
            super(ct, "pushLog.db", null, DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE + " ("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TYPE + " TEXT NOT NULL,"
                    + TITLE + " TEXT NOT NULL,"
                    + MSG + " TEXT NOT NULL);";

            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + TABLE);
            onCreate(db);
        }
    }
}
