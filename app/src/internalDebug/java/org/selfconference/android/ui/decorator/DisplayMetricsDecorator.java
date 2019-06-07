package org.selfconference.ui.decorator;

import android.util.DisplayMetrics;

public final class DisplayMetricsDecorator {

  private final DisplayMetrics displayMetrics;

  public static DisplayMetricsDecorator decorate(DisplayMetrics displayMetrics) {
    return new DisplayMetricsDecorator(displayMetrics);
  }

  private DisplayMetricsDecorator(DisplayMetrics displayMetrics) {
    this.displayMetrics = displayMetrics;
  }

  public String resolution() {
    return displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
  }

  public String density() {
    return displayMetrics.densityDpi + "dpi (" + densityBucket() + ")";
  }

  private String densityBucket() {
    switch (displayMetrics.densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "ldpi";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhdpi";
      case DisplayMetrics.DENSITY_XXHIGH:
        return "xxhdpi";
      case DisplayMetrics.DENSITY_XXXHIGH:
        return "xxxhdpi";
      case DisplayMetrics.DENSITY_TV:
        return "tvdpi";
      default:
        return String.valueOf(displayMetrics.densityDpi);
    }
  }
}
