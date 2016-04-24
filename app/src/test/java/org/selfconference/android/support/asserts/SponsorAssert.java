package org.selfconference.android.support.asserts;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.List;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;

public final class SponsorAssert extends AbstractAssert<SponsorAssert, Sponsor> {

  public static SponsorAssert assertThat(Sponsor actual) {
    return new SponsorAssert(actual);
  }

  protected SponsorAssert(Sponsor actual) {
    super(actual, SponsorAssert.class);
  }

  public SponsorAssert hasId(int id) {
    Assertions.assertThat(actual.id()).isEqualTo(id);

    return this;
  }

  public SponsorAssert hasName(String name) {
    Assertions.assertThat(actual.name()).isEqualTo(name);

    return this;
  }

  public SponsorAssert hasLink(String link) {
    Assertions.assertThat(actual.link()).isEqualTo(link);

    return this;
  }

  public SponsorAssert hasPhoto(String photo) {
    Assertions.assertThat(actual.photo()).isEqualTo(photo);

    return this;
  }

  public SponsorAssert hasSponsorLevels(String... names) {
    Function<SponsorLevel, String> sponsorLevelNameFunction = new Function<SponsorLevel, String>() {
      @Override public String apply(SponsorLevel sponsorLevel) {
        return sponsorLevel.name();
      }
    };
    List<String> sponsorLevelNames =
        Lists.transform(actual.sponsorLevels(), sponsorLevelNameFunction);
    Assertions.assertThat(names).containsAll(sponsorLevelNames);

    return this;
  }

  public SponsorAssert hasNumberOfSponsorLevels(int size) {
    Assertions.assertThat(actual.sponsorLevels()).hasSize(size);

    return this;
  }
}
