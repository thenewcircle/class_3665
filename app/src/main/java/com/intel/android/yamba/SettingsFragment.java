package com.intel.android.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Dave Smith
 * Date: 9/26/14
 * SettingsFragment
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
