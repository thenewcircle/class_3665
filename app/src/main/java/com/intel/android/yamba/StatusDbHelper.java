package com.intel.android.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Dave Smith
 * Date: 9/24/14
 * StatusDbHelper
 */
public class StatusDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "yamba.db";
    private static final int DB_VERSION = 1;

    public StatusDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s TEXT, %s TEXT);",
                StatusContract.TABLE_NAME,
                StatusContract.StatusColumns._ID, StatusContract.StatusColumns.CREATED_AT,
                StatusContract.StatusColumns.USER, StatusContract.StatusColumns.MESSAGE);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = String.format("DROP TABLE IF EXISTS %s;", StatusContract.TABLE_NAME);

        db.execSQL(sql);
        onCreate(db);
    }
}
