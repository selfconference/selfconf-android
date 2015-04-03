package org.selfconference.android.settings;

import android.os.Bundle;

import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;

public class SettingsActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(getToolbar());
        setStatusBarColor(getResources().getColor(R.color.primary_dark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
