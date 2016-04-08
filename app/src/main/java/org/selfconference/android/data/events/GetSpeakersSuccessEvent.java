package org.selfconference.android.data.events;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.speakers.Speaker;

public final class GetSpeakersSuccessEvent {
  public final ImmutableList<Speaker> speakers;

  public GetSpeakersSuccessEvent(ImmutableList<Speaker> speakers) {
    this.speakers = speakers;
  }
}
