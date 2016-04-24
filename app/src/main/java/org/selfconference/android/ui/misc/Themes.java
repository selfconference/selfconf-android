package org.selfconference.android.ui.misc;

import android.support.annotation.StyleRes;
import org.selfconference.android.R;

public final class Themes {
  private static final int[] THEMES = {
      R.style.GreenTheme, //
      R.style.OrangeTheme, //
      R.style.PurpleTheme, //
      R.style.RedTheme, //
  };

  /**
   * Returns a Self.conference-branded theme for a given id.
   *
   * @param id The id to map to a position in the {@link #THEMES} array.
   * @return The theme style resource id.
   */
  @StyleRes public static int forId(int id) {
    int index = Math.abs(id);
    return THEMES[index % THEMES.length];
  }

  private Themes() {
    throw new AssertionError("No instances.");
  }
}
