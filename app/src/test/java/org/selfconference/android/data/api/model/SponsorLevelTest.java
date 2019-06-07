package org.selfconference.data.api.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.selfconference.support.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.support.Parceler.testParceling;

@RunWith(RobolectricTestRunner.class)
public final class SponsorLevelTest {

  @Test public void sponsorLevelParcelsWithoutErrors() {
    SponsorLevel sponsorLevel = SponsorLevel.builder().id(1).order(1).name("Gold").build();

    Container<SponsorLevel> sponsorLevelContainer =
        testParceling(sponsorLevel, AutoValue_SponsorLevel.CREATOR);

    assertThat(sponsorLevelContainer.original).isEqualTo(sponsorLevelContainer.parceled);
  }
}
