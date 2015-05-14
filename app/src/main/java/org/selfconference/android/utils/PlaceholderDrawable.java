package org.selfconference.android.utils;

import com.amulyakhare.textdrawable.TextDrawable;

import org.selfconference.android.speakers.Speaker;

import static org.selfconference.android.utils.BrandColors.getPrimaryColorForPosition;

public final class PlaceholderDrawable {

    public static TextDrawable forSpeaker(Speaker speaker) {
        final String firstInitial = speaker.getName().substring(0, 1);
        final int backgroundColor = getPrimaryColorForPosition(speaker.getId());
        return TextDrawable.builder().buildRound(firstInitial, backgroundColor);
    }

    private PlaceholderDrawable() {}
}
