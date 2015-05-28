package org.selfconference.android.drawer;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.selfconference.android.R;

public enum DrawerItem {
    SESSIONS(R.drawable.ic_action_schedule, R.string.sessions),
    SPEAKERS(R.drawable.ic_social_mood, R.string.speakers),
    SPONSORS(R.drawable.ic_action_wallet_giftcard, R.string.sponsors),
    CODE_OF_CONDUCT(R.drawable.ic_action_description, R.string.code_of_conduct),
    SETTINGS(R.drawable.ic_action_settings, R.string.settings);

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
