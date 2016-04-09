package org.selfconference.android.util;

import com.amulyakhare.textdrawable.TextDrawable;
import org.selfconference.android.ui.misc.BrandColor;
import org.selfconference.android.data.api.model.Speaker;

public final class PlaceholderDrawable {

  public static TextDrawable forSpeaker(Speaker speaker) {
    BrandColor brandColor = BrandColor.makeBrand(speaker.id());
    String firstInitial = speaker.name().substring(0, 1);
    return TextDrawable.builder().buildRound(firstInitial, brandColor.getPrimary());
  }

  private PlaceholderDrawable() {
    throw new AssertionError("No instances.");
  }
}
