package com.intel.android.yamba;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


public class StatusDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        final Intent intent = getIntent();
        final Uri content = intent.getData();

        StatusDetailFragment fragment = StatusDetailFragment.newInstance(content);
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_details, fragment)
                .commit();
    }
}
