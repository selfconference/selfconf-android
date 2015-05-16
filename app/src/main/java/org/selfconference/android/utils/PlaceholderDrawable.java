package org.selfconference.android.utils;

import com.amulyakhare.textdrawable.TextDrawable;

import org.selfconference.android.speakers.Speaker;

public final class PlaceholderDrawable {

    public static TextDrawable forSpeaker(Speaker speaker) {
        final String firstInitial = speaker.getName().substring(0, 1);
        return TextDrawable.builder().buildRound(firstInitial, speaker.getBrandColor().getPrimary());
    }

    private PlaceholderDrawable() {
        throw new AssertionError("No instances.");
    }
}
