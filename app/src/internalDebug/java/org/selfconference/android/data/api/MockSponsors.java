package org.selfconference.android.data.api;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.selfconference.android.data.api.model.Sponsor;

import static org.selfconference.android.data.api.MockSponsorLevels.COOL;
import static org.selfconference.android.data.api.MockSponsorLevels.GNARLY;
import static org.selfconference.android.data.api.MockSponsorLevels.NEATO;
import static org.selfconference.android.data.api.MockSponsorLevels.WICKED_AWESOME;

final class MockSponsors {

  private static final Sponsor ONE = Sponsor.builder() //
      .id(1) //
      .name("One") //
      .link("http://example.com/sponsor/one") //
      .photo("mock:///avatar/one.png") //
      .sponsor_levels(ImmutableList.of(WICKED_AWESOME)) //
      .build();

  private static final Sponsor TWO = Sponsor.builder() //
      .id(2) //
      .name("Two") //
      .link("http://example.com/sponsor/two") //
      .photo("mock:///avatar/two.png") //
      .sponsor_levels(ImmutableList.of(GNARLY, COOL)) //
      .build();

  private static final Sponsor THREE = Sponsor.builder() //
      .id(3) //
      .name("Three") //
      .link("http://example.com/sponsor/three") //
      .photo("mock:///avatar/two.png") //
      .sponsor_levels(ImmutableList.of(NEATO)) //
      .build();

  static List<Sponsor> allSponsors() {
    return ImmutableList.of(ONE, TWO, THREE);
  }

  private MockSponsors() {
    throw new AssertionError("No instances.");
  }
}
