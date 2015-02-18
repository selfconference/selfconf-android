package org.selfconference.android.drawer;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.selfconference.android.R;

public enum DrawerItem {
    SCHEDULE(R.drawable.ic_schedule, R.string.schedule),
    SPEAKERS(R.drawable.ic_schedule, R.string.speakers);

    private final int icon;
    private final int title;

    DrawerItem(@DrawableRes int icon, @StringRes int title) {
        this.icon = icon;
        this.title = title;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    @StringRes
    public int getTitle() {
        return title;
    }
}
