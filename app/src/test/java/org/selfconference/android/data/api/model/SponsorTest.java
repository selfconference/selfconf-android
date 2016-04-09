package org.selfconference.android.data.api.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.support.Parceler.Container;

import static org.selfconference.android.support.Parceler.testParceling;
import static org.selfconference.android.support.asserts.SponsorAssert.assertThat;

@RunWith(CustomTestRunner.class) //
public final class SponsorTest {

  @Test public void sponsorParcelsWithoutError() {
    Sponsor sponsor = Sponsor.builder()
        .id(7)
        .name("Apprend")
        .photo("https://apprend.org/")
        .link("https://s3.amazonaws.com/selfconf/sponsors/apprend.png")
        .build();

    Container<Sponsor> sponsorContainer = testParceling(sponsor, AutoValue_Sponsor.CREATOR);

    assertThat(sponsorContainer.original).isEqualTo(sponsorContainer.parceled);
  }
}
