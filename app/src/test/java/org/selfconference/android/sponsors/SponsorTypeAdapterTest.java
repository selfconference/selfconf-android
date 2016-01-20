package org.selfconference.android.sponsors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.selfconference.android.sponsors.SponsorAssert.assertThat;

public final class SponsorTypeAdapterTest {
  private static final Gson GSON =
      new GsonBuilder().registerTypeAdapter(Sponsor.class, new SponsorTypeAdapter()).create();

  @Test public void duplicateSponsorLevelsAreRemovedWhenSerializingFromJson() {
    Sponsor sponsor = GSON.fromJson(sponsorWithDuplicateSponsorLevelsJson(), Sponsor.class);

    assertThat(sponsor) //
        .hasId(3)
        .hasName("Detroit Labs")
        .hasLink("http://detroitlabs.com")
        .hasPhoto("http://s3.amazonaws.com/selfconf/sponsors/dl.png")
        .hasNumberOfSponsorLevels(3);
  }

  private static String sponsorWithDuplicateSponsorLevelsJson() {
    return "{\n" +
        "  \"id\":3,\n" +
        "  \"name\":\"Detroit Labs\",\n" +
        "  \"link\":\"http://detroitlabs.com\",\n" +
        "  \"photo\":\"http://s3.amazonaws.com/selfconf/sponsors/dl.png\",\n" +
        "  \"event_id\":1,\n" +
        "  \"created_at\":\"2015-03-02T14:50:48.570Z\",\n" +
        "  \"updated_at\":\"2015-03-02T14:50:48.570Z\",\n" +
        "  \"sponsor_levels\":[\n" +
        "    {\n" +
        "      \"id\":7,\n" +
        "      \"name\":\"Diversity\",\n" +
        "      \"event_id\":1,\n" +
        "      \"created_at\":\"2015-03-02T14:50:48.425Z\",\n" +
        "      \"updated_at\":\"2015-03-02T14:50:48.425Z\",\n" +
        "      \"order\":7\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\":7,\n" +
        "      \"name\":\"Diversity\",\n" +
        "      \"event_id\":1,\n" +
        "      \"created_at\":\"2015-03-02T14:50:48.425Z\",\n" +
        "      \"updated_at\":\"2015-03-02T14:50:48.425Z\",\n" +
        "      \"order\":7\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\":9,\n" +
        "      \"name\":\"Lanyard\",\n" +
        "      \"event_id\":1,\n" +
        "      \"created_at\":\"2015-03-02T14:50:48.442Z\",\n" +
        "      \"updated_at\":\"2015-03-02T14:50:48.442Z\",\n" +
        "      \"order\":9\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\":12,\n" +
        "      \"name\":\"Snack\",\n" +
        "      \"event_id\":1,\n" +
        "      \"created_at\":\"2015-03-02T14:50:48.476Z\",\n" +
        "      \"updated_at\":\"2015-03-02T14:50:48.476Z\",\n" +
        "      \"order\":12\n" +
        "    }\n" +
        "  ]\n" +
        "}";
  }
}
