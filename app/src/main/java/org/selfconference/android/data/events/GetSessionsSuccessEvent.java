package org.selfconference.android.data.events;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.session.Session;

/** Posts when a {@link org.selfconference.android.data.jobs.GetSessionsJob} succeeds. */
public final class GetSessionsSuccessEvent {

  public final ImmutableList<Session> sessions;

  public GetSessionsSuccessEvent(ImmutableList<Session> sessions) {
    this.sessions = sessions;
  }
}
