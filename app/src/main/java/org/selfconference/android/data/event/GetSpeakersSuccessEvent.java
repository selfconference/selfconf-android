package org.selfconference.android.data.event;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.data.api.model.Speaker;

/** Posts when a {@link org.selfconference.android.data.job.GetSpeakersJob} succeeds. */
public final class GetSpeakersSuccessEvent {
  public final ImmutableList<Speaker> speakers;

  public GetSpeakersSuccessEvent(ImmutableList<Speaker> speakers) {
    this.speakers = speakers;
  }
}
