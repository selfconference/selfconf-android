package org.selfconference.android.utils;

import com.amulyakhare.textdrawable.TextDrawable;
import org.selfconference.android.brand.BrandColor;
import org.selfconference.android.speakers.Speaker;

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
