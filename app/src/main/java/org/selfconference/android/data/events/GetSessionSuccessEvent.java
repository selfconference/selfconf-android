package org.selfconference.android.data.events;

import org.selfconference.android.session.Session;

/** Posts when a {@link org.selfconference.android.data.jobs.GetSessionJob} succeeds. */
public final class GetSessionSuccessEvent {

  public final Session session;

  public GetSessionSuccessEvent(Session session) {
    this.session = session;
  }
}
