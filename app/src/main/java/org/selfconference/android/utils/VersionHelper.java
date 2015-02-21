package org.selfconference.android.utils;

import android.graphics.drawable.Drawable;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public final class VersionHelper {
    public static void setDrawableTint(Drawable drawable, int color) {
        if (SDK_INT >= LOLLIPOP) {
            drawable.setTint(color);
        }
    }

}
