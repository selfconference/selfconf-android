package org.selfconference.android.sponsors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.Parceler.testParceling;

@RunWith(CustomTestRunner.class)
public class SponsorLevelTest {

    @Test
    public void sponsorLevelParcelsWithoutErrors() {
        final SponsorLevel sponsorLevel = SponsorLevel.builder()
                .id(1)
                .order(1)
                .name("Gold")
                .build();

        final Container<SponsorLevel> sponsorLevelContainer = testParceling(sponsorLevel, SponsorLevel.CREATOR);

        assertThat(sponsorLevelContainer.original).isEqualTo(sponsorLevelContainer.parceled);
    }
}