package org.selfconference.android.utils;

import android.content.res.Resources;
import android.support.annotation.ColorRes;

import org.selfconference.android.App;

public final class ResourceProvider {
    private static final Resources RESOURCES = App.getInstance().getResources();

    public static int getColor(@ColorRes int colorResId) {
        return RESOURCES.getColor(colorResId);
    }

    private ResourceProvider() {}
}
