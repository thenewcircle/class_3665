package com.intel.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class StatusUpdateService extends IntentService {
    private static final int NOTE_ID = 1001;
    public static final String KEY_MESSAGE = "message";

    private YambaClient mYambaClient;
    private NotificationManager mNotificationManager;

    public StatusUpdateService() {
        super("StatusUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mYambaClient = new YambaClient("student", "password");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /** This is called on a background thread */
    @Override
    protected void onHandleIntent(Intent intent) {
        String status = intent.getStringExtra(KEY_MESSAGE);

        Notification note = new Notification.Builder(this)
                .setContentTitle("Posting Status...")
                .setContentText("Posting your status to Yamba")
                .setTicker("Posting...")
                .setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true)
                .build();

        mNotificationManager.notify(NOTE_ID, note);
        try {
            mYambaClient.postStatus(status);
            mNotificationManager.cancel(NOTE_ID);
        } catch (YambaClientException e) {
            e.printStackTrace();

            Intent errorIntent = new Intent(this, StatusActivity.class);
            errorIntent.putExtra(KEY_MESSAGE, status);

            PendingIntent trigger =
                    PendingIntent.getActivity(this, 0, errorIntent, 0);

            note = new Notification.Builder(this)
                    .setContentTitle("Error Posting Status...")
                    .setContentText("Unable to post. Tap here to retry.")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(trigger)
                    .setAutoCancel(true)
                    .build();
            mNotificationManager.notify(NOTE_ID, note);
        }
    }
}
