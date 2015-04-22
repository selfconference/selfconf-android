package org.selfconference.android.utils;

import org.selfconference.android.R;

import static org.selfconference.android.utils.ResourceProvider.getColor;

public final class BrandColors {
    private static final int[][] COLORS = new int[][]{
            {R.color.primary, R.color.primary_dark},
            {R.color.accent, R.color.accent_dark},
            {R.color.purple, R.color.purple_dark},
            {R.color.red, R.color.red_dark}
    };

    public static int getPrimaryColorForPosition(int position) {
        return getColor(COLORS[position % COLORS.length][0]);
    }

    public static int getSecondaryColorForPosition(int position) {
        return getColor(COLORS[position % COLORS.length][1]);
    }

    private BrandColors() {
    }
}
