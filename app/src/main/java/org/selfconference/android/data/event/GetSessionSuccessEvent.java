package org.selfconference.android.data.event;

import org.selfconference.android.data.api.model.Session;

/** Posts when a {@link org.selfconference.android.data.job.GetSessionJob} succeeds. */
public final class GetSessionSuccessEvent {

  public final Session session;

  public GetSessionSuccessEvent(Session session) {
    this.session = session;
  }
}
