package org.selfconference.android.utils;

import android.content.Context;

import org.selfconference.android.R;

public final class BrandColors {
    private static final int[] COLORS = new int[]{
            R.color.red,
            R.color.accent,
            R.color.yellow,
            R.color.primary,
            R.color.purple
    };

    public static int getColorForPosition(Context context, int position) {
        return context.getResources().getColor(COLORS[position % COLORS.length]);
    }

    private BrandColors() {
    }
}
