package org.selfconference.android.data.api;

import org.selfconference.android.data.api.model.Organizer;

final class MockOrganizers {

  static final Organizer JOE_ROBIT = Organizer.builder() //
      .id(1) //
      .name("Joe Robit") //
      .email("joe.robit@selfconference.org") //
      .twitter("joerobit") //
      .bio("Joe Robit is a, in fact, a robit.") //
      .photo("mock:///avatar/one.png") //
      .build();

  static final Organizer ROBO_SCOTT = Organizer.builder() //
      .id(2) //
      .name("Robo Scott") //
      .email("robo.scott@selfconference.org") //
      .twitter("roboscott") //
      .bio("Robo Scott is a robit.") //
      .photo("mock:///avatar/two.png") //
      .build();

  static final Organizer PAT_BOT = Organizer.builder() //
      .id(3) //
      .name("Pat Bot") //
      .email("pat.bot@selfconference.org") //
      .twitter("patbot") //
      .bio("Pat Bot is a robit.") //
      .photo("mock:///avatar/three.png") //
      .build();

  private MockOrganizers() {
    throw new AssertionError("No instances.");
  }
}
