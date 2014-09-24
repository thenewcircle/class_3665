package com.intel.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RefreshService extends IntentService {

    private static final int NOTE_ID = 999;

    private YambaClient mYambaClient;
    private NotificationManager mNotificationManager;

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mYambaClient = new YambaClient("student", "password");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                List<YambaClient.Status> timeline = mYambaClient.getTimeline(20);
                ContentValues[] values = new ContentValues[timeline.size()];
                Log.d("Yamba", "Inserting data into ContentValues");
                for (int i=0; i < values.length; i++) {
                    YambaClient.Status status = timeline.get(i);
                    ContentValues itemValue = new ContentValues();

                    itemValue.put(StatusContract.StatusColumns._ID, status.getId());
                    itemValue.put(StatusContract.StatusColumns.CREATED_AT, status.getCreatedAt().getTime());
                    itemValue.put(StatusContract.StatusColumns.USER, status.getUser());
                    itemValue.put(StatusContract.StatusColumns.MESSAGE, status.getMessage());

                    values[i] = itemValue;
                }
                Log.d("Yamba", "BulkInsert into provider");
                Uri uri = Uri.withAppendedPath(StatusContract.CONTENT_URI, StatusContract.TABLE_NAME);
                int affected = getContentResolver().bulkInsert(uri, values);

                Notification note = new Notification.Builder(this)
                        .setContentTitle("New Status Updates...")
                        .setContentText(String.format("You have %d new updates.", affected))
                        .setTicker("New Status Updates...")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .build();

                mNotificationManager.notify(NOTE_ID, note);

            } catch (YambaClientException e) {
                e.printStackTrace();
            }
        }
    }
}
