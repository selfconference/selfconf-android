package org.selfconference.util;

import android.os.Build;
import androidx.annotation.DrawableRes;
import org.selfconference.R;

public final class PlaceholderDrawable {

  /** Robits!!! */
  private static final int[] ROBITS = {
      R.drawable.robit1, //
      R.drawable.robit2, //
      R.drawable.robit3, //
      R.drawable.robit4, //
      R.drawable.robit5, //
      R.drawable.robit6, //
  };

  /**
   * Returns a placeholder drawable resource reference for use
   * by {@link com.squareup.picasso.Picasso}.
   *
   * This method requires an id parameter to map an object's id to a position in the array
   * of placeholder drawables.
   * For example, supplying {@link org.selfconference.data.api.model.Speaker#id()} to this
   * method will return a consistent placeholder drawable for that object.
   *
   * @param id The id to map to a position in the {@link #ROBITS} array.
   * @return The placeholder drawable resource id.
   */
  @DrawableRes public static int forId(int id) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      return R.drawable.support_robit;
    }
    int index = Math.abs(id);
    return ROBITS[index % ROBITS.length];
  }

  private PlaceholderDrawable() {
    throw new AssertionError("No instances.");
  }
}
