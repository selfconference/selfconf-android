package org.selfconference.android.data.events;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.session.Session;

public final class GetSessionsSuccessEvent {

  public final ImmutableList<Session> sessions;

  public GetSessionsSuccessEvent(ImmutableList<Session> sessions) {
    this.sessions = sessions;
  }
}
