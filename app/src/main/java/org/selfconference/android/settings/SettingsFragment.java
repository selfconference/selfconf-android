package org.selfconference.android.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import org.selfconference.android.R;

import static org.selfconference.android.BuildConfig.VERSION_NAME;

public final class SettingsFragment extends PreferenceFragment {

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);

    findPreference(getString(R.string.key_app_version)) //
        .setSummary(getString(R.string.summary_app_version, VERSION_NAME));
  }
}
