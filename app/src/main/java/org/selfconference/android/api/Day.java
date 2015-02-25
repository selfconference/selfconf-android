package org.selfconference.android.api;

public enum Day {
    ONE,
    TWO;

    public static Day fromPosition(final int position) {
        switch (position) {
            case 0:  return ONE;
            case 1:  return TWO;
            default: throw new IllegalArgumentException("position must be 0 or 1 but position == " + position);
        }
    }
}
