package org.selfconference.android.data.api;

import org.selfconference.android.data.api.model.SponsorLevel;

final class MockSponsorLevels {

  static final SponsorLevel WICKED_AWESOME = SponsorLevel.builder() //
      .id(1) //
      .order(1) //
      .name("Wicked Awesome") //
      .build();

  static final SponsorLevel GNARLY = SponsorLevel.builder() //
      .id(2) //
      .order(2) //
      .name("Gnarly") //
      .build();

  static final SponsorLevel COOL = SponsorLevel.builder() //
      .id(3) //
      .order(3) //
      .name("Cool") //
      .build();

  static final SponsorLevel NEATO = SponsorLevel.builder() //
      .id(4) //
      .order(4) //
      .name("Neato") //
      .build();

  private MockSponsorLevels() {
    throw new AssertionError("No instances.");
  }
}
