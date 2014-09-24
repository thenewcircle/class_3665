package com.intel.android.yamba;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Dave Smith
 * Date: 9/24/14
 * StatusContract
 */
public final class StatusContract {

    public static final String TABLE_NAME = "status";
    public static final String AUTHORITY = "com.intel.android.yamba.provider";

    /* Public Exposed Content URI */
    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();


    /* Data MIME Types */
    public static final String CONTENT_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.intel.android.yamba";
    public static final String CONTENT_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.intel.android.yamba";

    public static final class StatusColumns implements BaseColumns {
        public static final String CREATED_AT = "created_at";
        public static final String USER = "user";
        public static final String MESSAGE = "message";
    }

    /* Default Sorting Order for Cursors */
    public static final String DEFAUT_SORT_ORDER = StatusColumns.CREATED_AT + " DESC";
}
