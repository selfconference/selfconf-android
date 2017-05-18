package org.selfconference.android.data.api;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.selfconference.android.data.api.model.Speaker;

final class MockSpeakers {

  static final Speaker KOBE_BRYANT = Speaker.builder() //
      .id(5) //
      .name("Kobe Bryant") //
      .bio("Testing") //
      .twitter("kobebryant") //
      .headshot("http://a.espncdn.com/combiner/i?img=/i/headshots/nba/players/full/110.png") //
      .sessions(ImmutableList.of(MockSessions.findSessionById(1))) //
      .build();

  static List<Speaker> allSpeakers() {
    return ImmutableList.of(KOBE_BRYANT);
  }

  private MockSpeakers() {
    throw new AssertionError("No instances.");
  }
}
