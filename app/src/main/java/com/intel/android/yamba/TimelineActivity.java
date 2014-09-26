package com.intel.android.yamba;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TimelineActivity extends Activity implements
        TimelineFragment.OnFragmentInteractionListener {

    private static boolean mInTimeline = false;
    public static boolean isInTimeline() {
        return mInTimeline;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInTimeline = true;

        //Clear any status update notification
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(RefreshService.NOTE_ID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInTimeline = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_refresh) {
            Intent intent = new Intent(this, RefreshService.class);
            startService(intent);
            return true;
        }

        if (id == R.id.action_post) {
            Intent intent = new Intent(this, StatusActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        if (findViewById(R.id.fragment_details) == null) {
            Intent intent = new Intent(this, StatusDetailActivity.class);
            intent.setData(uri);

            startActivity(intent);
        } else {
            //We are in two-pane mode
            StatusDetailFragment fragment = StatusDetailFragment.newInstance(uri);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_details, fragment)
                    .commit();
        }
    }
}
