package org.selfconference.android.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import org.selfconference.android.App;

public final class ResourceProvider {
  private static final Resources RESOURCES = App.getInstance().getResources();

  public static int getColor(@ColorRes int colorResId) {
    return ContextCompat.getColor(App.getInstance(), colorResId);
  }

  public static String getString(@StringRes int stringResId, Object... formatArgs) {
    return RESOURCES.getString(stringResId, formatArgs);
  }

  public static String getQuantityString(@PluralsRes int pluralsResId, int quantity,
      Object... formatArgs) {
    return RESOURCES.getQuantityString(pluralsResId, quantity, formatArgs);
  }

  public static Drawable getDrawable(@DrawableRes int drawableResId) {
    return ContextCompat.getDrawable(App.getInstance(), drawableResId);
  }

  private ResourceProvider() {
    throw new AssertionError("No instances.");
  }
}
