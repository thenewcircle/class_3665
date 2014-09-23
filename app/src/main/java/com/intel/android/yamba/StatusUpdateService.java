package com.intel.android.yamba;

import android.app.IntentService;
import android.content.Intent;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class StatusUpdateService extends IntentService {
    public static final String KEY_MESSAGE = "message";

    private YambaClient mYambaClient;

    public StatusUpdateService() {
        super("StatusUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mYambaClient = new YambaClient("student", "password");
    }

    /** This is called on a background thread */
    @Override
    protected void onHandleIntent(Intent intent) {
        String status = intent.getStringExtra(KEY_MESSAGE);

        try {
            mYambaClient.postStatus(status);
        } catch (YambaClientException e) {
            e.printStackTrace();
        }
    }
}
