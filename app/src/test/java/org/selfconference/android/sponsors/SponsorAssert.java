package org.selfconference.android.sponsors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public final class SponsorAssert extends AbstractAssert<SponsorAssert, Sponsor> {

    public static SponsorAssert assertThat(Sponsor actual) {
        return new SponsorAssert(actual);
    }

    protected SponsorAssert(Sponsor actual) {
        super(actual, SponsorAssert.class);
    }

    public SponsorAssert hasId(int id) {
        Assertions.assertThat(actual.getId())
                .isEqualTo(id);

        return this;
    }

    public SponsorAssert hasName(String name) {
        Assertions.assertThat(actual.getName())
                .isEqualTo(name);

        return this;
    }

    public SponsorAssert hasLink(String link) {
        Assertions.assertThat(actual.getLink())
                .isEqualTo(link);

        return this;
    }

    public SponsorAssert hasPhoto(String photo) {
        Assertions.assertThat(actual.getPhoto())
                .isEqualTo(photo);

        return this;
    }

    public SponsorAssert hasNumberOfSponsorLevels(int size) {
        Assertions.assertThat(actual.getSponsorLevels())
                .hasSize(size);

        return this;
    }
}
