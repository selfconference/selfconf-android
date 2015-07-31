package org.selfconference.android.sponsors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;

import static org.selfconference.android.Parceler.testParceling;
import static org.selfconference.android.sponsors.SponsorAssert.assertThat;

@RunWith(CustomTestRunner.class) public class SponsorTest {

  @Test public void sponsorParcelsWithoutError() {
    final Sponsor sponsor = Sponsor.builder()
        .id(7)
        .name("Apprend")
        .photo("https://apprend.org/")
        .link("https://s3.amazonaws.com/selfconf/sponsors/apprend.png")
        .build();

    final Container<Sponsor> sponsorContainer = testParceling(sponsor, Sponsor.CREATOR);

    assertThat(sponsorContainer.original).isEqualTo(sponsorContainer.parceled);
  }
}