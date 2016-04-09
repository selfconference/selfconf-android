package org.selfconference.android.data.event;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.data.api.model.Session;

/** Posts when a {@link org.selfconference.android.data.job.GetSessionsJob} succeeds. */
public final class GetSessionsSuccessEvent {

  public final ImmutableList<Session> sessions;

  public GetSessionsSuccessEvent(ImmutableList<Session> sessions) {
    this.sessions = sessions;
  }
}
