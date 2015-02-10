package org.selfconference.android.utils;

import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.Window;

public final class StatusBarHelper {

    public static void setStatusBarColor(Window window, @ColorRes int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int statusBarColor = window.getContext().getResources().getColor(colorResId);
            window.setStatusBarColor(statusBarColor);
        }
    }

    private StatusBarHelper() {
    }
}
