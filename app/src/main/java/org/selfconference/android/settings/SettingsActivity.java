package org.selfconference.android.settings;

import android.os.Bundle;
import android.view.MenuItem;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;

import static org.selfconference.android.utils.ResourceProvider.getColor;

public class SettingsActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    setSupportActionBar(getToolbar());
    setStatusBarColor(getColor(R.color.green_dark));
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
