package org.selfconference.android.data.event;

import java.util.List;
import org.selfconference.android.data.api.model.Speaker;

/** Posts when a {@link org.selfconference.android.data.job.GetSpeakersJob} succeeds. */
public final class GetSpeakersSuccessEvent {
  public final List<Speaker> speakers;

  public GetSpeakersSuccessEvent(List<Speaker> speakers) {
    this.speakers = speakers;
  }
}
