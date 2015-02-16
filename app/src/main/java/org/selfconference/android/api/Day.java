package org.selfconference.android.api;

public enum Day {
    ONE,
    TWO;

    public static Day fromPosition(final int position) {
        if (position == 0) {
            return ONE;
        } else if (position == 1) {
            return TWO;
        }
        throw new IllegalArgumentException("position must be 0 or 1 but position == " + position);
    }
}
