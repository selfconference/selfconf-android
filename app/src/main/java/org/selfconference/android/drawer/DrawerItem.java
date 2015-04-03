package org.selfconference.android.drawer;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.selfconference.android.R;

public enum DrawerItem {
    SCHEDULE(R.drawable.ic_schedule, R.string.schedule),
    SPEAKERS(R.drawable.ic_mood_grey600_24dp, R.string.speakers),
    CODE_OF_CONDUCT(R.drawable.ic_description_grey600_24dp, R.string.code_of_conduct),
    SETTINGS(R.drawable.ic_settings_grey600_24dp, R.string.settings);

    private final int icon;
    private final int title;

    DrawerItem(@DrawableRes int icon, @StringRes int title) {
        this.icon = icon;
        this.title = title;
    }

    @DrawableRes public int getIcon() {
        return icon;
    }

    @StringRes public int getTitle() {
        return title;
    }
}
