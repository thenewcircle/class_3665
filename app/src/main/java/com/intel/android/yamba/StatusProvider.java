package com.intel.android.yamba;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StatusProvider extends ContentProvider {

    private static final int MATCH_ITEM = 1;
    private static final int MATCH_LIST = 2;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE_NAME, MATCH_LIST);
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE_NAME + "/#", MATCH_ITEM);
    }

    private StatusDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new StatusDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_LIST:
                return StatusContract.CONTENT_DIR;
            case MATCH_ITEM:
                return StatusContract.CONTENT_ITEM;
            default:
                return null;
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_LIST:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                long id = db.insertWithOnConflict(StatusContract.TABLE_NAME, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    //When insert successful, notify observers
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(StatusContract.CONTENT_URI, id);
                } else {
                    return null;
                }
            default:
                throw new IllegalArgumentException("Cannot insert an existing record.");
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (sUriMatcher.match(uri)) {
            case MATCH_LIST:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                int inserted = 0;
                try {
                    db.beginTransaction();

                    for (ContentValues item : values) {
                        long id = db.insertWithOnConflict(StatusContract.TABLE_NAME, null, item,
                                SQLiteDatabase.CONFLICT_IGNORE);
                        if (id > 0) {
                            inserted++;
                        }
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    //Reset count to invalid value
                    inserted = -1;
                } finally {
                    db.endTransaction();
                }

                if (inserted > 0) {
                    //When insert successful, notify observers
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return inserted;
            default:
                throw new IllegalArgumentException("Cannot insert an existing record.");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result;

        switch (sUriMatcher.match(uri)) {
            case MATCH_LIST:
                result = db.query(StatusContract.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case MATCH_ITEM:
                long id = ContentUris.parseId(uri);
                result = db.query(StatusContract.TABLE_NAME, projection,
                        StatusContract.StatusColumns._ID + " = ?",
                        new String[] {String.valueOf(id)},
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri Requested");
        }
        //Register this cursor to get notifications on changes
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
