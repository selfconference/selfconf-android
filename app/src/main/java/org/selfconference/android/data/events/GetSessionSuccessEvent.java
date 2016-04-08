package org.selfconference.android.data.events;

import org.selfconference.android.session.Session;

public final class GetSessionSuccessEvent {

  public final Session session;

  public GetSessionSuccessEvent(Session session) {
    this.session = session;
  }
}
