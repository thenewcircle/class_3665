package com.intel.android.yamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Dave Smith
 * Date: 9/26/14
 * YambaApplication
 */
public class YambaApplication extends Application implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        scheduleRefreshAlarm(this, prefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("prefAutoRefresh".equals(key)) {
            scheduleRefreshAlarm(this, sharedPreferences);
        }
    }

    public static void scheduleRefreshAlarm(Context context, SharedPreferences prefs) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        long interval;
        try {
            interval = Long.parseLong(prefs.getString("prefAutoRefresh", "0"));
        } catch (NumberFormatException e) {
            interval = 0;
        }

        Log.d("Yamba", "Scheduling Refresh Alarm");

        PendingIntent trigger = PendingIntent.getService(context, 0,
                new Intent(context, RefreshService.class), 0);

        if (interval == 0) {
            Log.d("Yamba", "Cancelling auto-refresh");
            manager.cancel(trigger);
        } else {
            Log.d("Yamba", "Scheduling alarm for "+interval+" ms");
            manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+500, interval, trigger);
        }
    }
}
