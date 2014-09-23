package com.intel.android.yamba;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
        mStatusText.setText(null);
    }

    public void onPostClick(View v) {
        Toast.makeText(this, mStatusText.getText(), Toast.LENGTH_SHORT).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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
