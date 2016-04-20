package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue public abstract class Sponsor implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Sponsor.Builder();
  }

  Sponsor() {}

  public abstract int id();

  public abstract String name();

  public abstract String link();

  public abstract String photo();

  // @ParcelAdapter(SponsorLevel.ImmutableListTypeAdapter.class)
  public abstract List<SponsorLevel> sponsorLevels();

  @AutoValue.Builder public abstract static class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder link(String link);

    public abstract Builder photo(String photo);

    public abstract Builder sponsorLevels(List<SponsorLevel> sponsorLevels);

    public abstract Sponsor build();
  }
}
