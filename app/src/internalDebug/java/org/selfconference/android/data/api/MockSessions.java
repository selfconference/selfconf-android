package org.selfconference.android.data.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.List;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.util.Instants;

import static org.selfconference.android.data.api.MockRooms.EARTH;
import static org.selfconference.android.data.api.MockSpeakers.KOBE_BRYANT;

final class MockSessions {

  private static final Session END_OF_AN_ERA = Session.builder() //
      .id(1) //
      .slot(Slot.builder()
          .id(1)
          .time(Instants.fromEstString("2016-05-20T09:00:00.000-05:00"))
          .build()) //
      .keynote(true) //
      .name("The End of an Era") //
      .description("Lorem ipsum should probably go here.") //
      .room(EARTH) //
      .speakers(Collections.singletonList(KOBE_BRYANT)) //
      .build();

  static List<Session> allSessions() {
    return ImmutableList.of(END_OF_AN_ERA);
  }

  static Session findSessionById(final int sessionId) {
    return Iterables.find(allSessions(), session -> session.id() == sessionId);
  }

  private MockSessions() {
    throw new AssertionError("No instances.");
  }
}
