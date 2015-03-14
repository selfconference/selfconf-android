package org.selfconference.android.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.selfconference.android.R;

import static android.preference.Preference.OnPreferenceChangeListener;
import static com.parse.ParsePush.subscribeInBackground;
import static com.parse.ParsePush.unsubscribeInBackground;
import static org.selfconference.android.BuildConfig.VERSION_NAME;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        findPreference(getString(R.string.key_app_version))
                .setSummary(getString(R.string.summary_app_version, VERSION_NAME));

        findPreference(getString(R.string.key_push_notifications))
                .setOnPreferenceChangeListener(onPushNotificationCheckChangeListener);
    }

    private final OnPreferenceChangeListener onPushNotificationCheckChangeListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            final boolean checked = (boolean) newValue;
            if (checked) {
                subscribeInBackground("all");
            } else {
                unsubscribeInBackground("all");
            }
            return true;
        }
    };
}
