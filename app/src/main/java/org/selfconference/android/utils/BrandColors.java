package org.selfconference.android.utils;

import android.content.Context;

import org.selfconference.android.R;

public final class BrandColors {
    private static final int[][] COLORS = new int[][]{
            {R.color.red, R.color.red_dark},
            {R.color.accent, R.color.accent_dark},
            {R.color.primary, R.color.primary_dark},
            {R.color.purple, R.color.purple_dark}
    };

    public static int getPrimaryColorForPosition(Context context, int position) {
        return context.getResources().getColor(COLORS[position % COLORS.length][0]);
    }

    public static int getSecondaryColorForPosition(Context context, int position) {
        return context.getResources().getColor(COLORS[position % COLORS.length][1]);
    }

    private BrandColors() {
    }
}
