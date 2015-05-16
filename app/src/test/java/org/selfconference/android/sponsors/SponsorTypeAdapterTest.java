package org.selfconference.android.sponsors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.Reader;

import static org.selfconference.android.FileLoader.loadJson;
import static org.selfconference.android.sponsors.SponsorAssert.assertThat;

public class SponsorTypeAdapterTest {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Sponsor.class, new SponsorTypeAdapter())
            .create();

    @Test
    public void duplicateSponsorLevelsAreRemovedWhenSerializingFromJson() {
        final Reader reader = loadJson("sponsor-with-multiple-sponsor-levels.json");
        final Sponsor sponsor = GSON.fromJson(reader, Sponsor.class);

        assertThat(sponsor)
                .hasId(3)
                .hasName("Detroit Labs")
                .hasLink("http://detroitlabs.com")
                .hasPhoto("http://s3.amazonaws.com/selfconf/sponsors/dl.png")
                .hasNumberOfSponsorLevels(3);
    }
}