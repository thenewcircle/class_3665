package com.intel.android.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends Activity implements TextWatcher {

    private static final int MAX_CHARS = 140;

    private EditText mStatusText;
    private TextView mCounterText;
    private Button mPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mStatusText = (EditText) findViewById(R.id.text_status);
        mCounterText = (TextView) findViewById(R.id.text_counter);
        mPostButton = (Button) findViewById(R.id.button_status);

        mStatusText.addTextChangedListener(this);

        final Intent intent = getIntent();
        if (intent.hasExtra(StatusUpdateService.KEY_MESSAGE)) {
            mStatusText.setText(intent.getStringExtra(StatusUpdateService.KEY_MESSAGE));
        } else {
            mStatusText.setText(null);
        }
    }

    public void onPostClick(View v) {
        Intent intent = new Intent(this, StatusUpdateService.class);
        intent.putExtra(StatusUpdateService.KEY_MESSAGE, mStatusText.getText().toString());

        startService(intent);

        mStatusText.getText().clear();
    }

    private class PostStatusTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(StatusActivity.this,
                    "Posting Status...", "Posting your Yamba Status.", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String status = params[0];

            YambaClient client = new YambaClient("student", "password");
            try {
                client.postStatus(status);
                return true;
            } catch (YambaClientException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if (result) {
                mStatusText.getText().clear();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
       public void afterTextChanged(Editable s) {
        int remaining = MAX_CHARS - mStatusText.length();
        mCounterText.setText(String.valueOf(remaining));

        boolean disabled = (remaining < 0);
        if (disabled) {
            mCounterText.setTextColor(Color.RED);
            mPostButton.setEnabled(false);
        } else {
            mCounterText.setTextColor(Color.BLACK);
            mPostButton.setEnabled(true);
        }
    }
}
