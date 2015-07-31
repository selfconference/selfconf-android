package org.selfconference.android;

import android.content.res.Resources;
import android.support.annotation.ColorRes;
import org.robolectric.Robolectric;

public final class ResourceUtils {
  private static final Resources RESOURCES = Robolectric.application.getResources();

  public static int getColor(@ColorRes int colorResId) {
    return RESOURCES.getColor(colorResId);
  }

  private ResourceUtils() {
  }
}
