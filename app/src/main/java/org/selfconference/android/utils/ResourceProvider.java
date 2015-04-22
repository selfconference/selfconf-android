package org.selfconference.android.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import org.selfconference.android.App;

public final class ResourceProvider {
    private static final Resources RESOURCES = App.getInstance().getResources();

    public static int getColor(@ColorRes int colorResId) {
        return RESOURCES.getColor(colorResId);
    }

    public static String getString(@StringRes int stringResId) {
        return RESOURCES.getString(stringResId);
    }

    public static Drawable getDrawable(@DrawableRes int drawableResId) {
        return RESOURCES.getDrawable(drawableResId);
    }

    private ResourceProvider() {}
}
