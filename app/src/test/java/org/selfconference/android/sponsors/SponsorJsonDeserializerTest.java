package org.selfconference.android.sponsors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.support.Sponsors;

import static org.selfconference.android.sponsors.SponsorAssert.assertThat;

public final class SponsorJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder() //
        .registerTypeAdapter(Sponsor.class, new SponsorJsonDeserializer())
        .registerTypeAdapter(SponsorLevel.class, new SponsorLevelJsonDeserializer())
        .create();
  }

  @Test public void serializesMultipleSponsorLevelsFromJson() {
    Sponsor sponsor = gson.fromJson(Sponsors.withMultipleSponsorLevels(), Sponsor.class);

    assertThat(sponsor) //
        .hasId(3)
        .hasName("Detroit Labs")
        .hasLink("http://detroitlabs.com")
        .hasPhoto("http://s3.amazonaws.com/selfconf/sponsors/dl.png")
        .hasSponsorLevels("Diversity", "Lanyard", "Snack");
  }
}
