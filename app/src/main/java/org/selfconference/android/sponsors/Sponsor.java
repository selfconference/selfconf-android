package org.selfconference.android.sponsors;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ryanharter.auto.value.parcel.ParcelAdapter;

@AutoValue public abstract class Sponsor implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Sponsor.Builder();
  }

  Sponsor() {}

  public abstract int id();

  public abstract String name();

  public abstract String link();

  public abstract String photo();

  @ParcelAdapter(ImmutableListSponsorLevelTypeAdapter.class)
  public abstract ImmutableList<SponsorLevel> sponsorLevels();

  @AutoValue.Builder abstract static class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder link(String link);

    public abstract Builder photo(String photo);

    public Builder addSponsorLevels(Iterable<SponsorLevel> sponsorLevels) {
      sponsorLevelsBuilder().addAll(sponsorLevels);
      return this;
    }

    abstract ImmutableList.Builder<SponsorLevel> sponsorLevelsBuilder();

    public abstract Sponsor build();
  }
}
