package org.selfconference.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;
import org.selfconference.data.api.SponsorComparator;

@AutoValue public abstract class Sponsor implements Parcelable, Comparable<Sponsor> {

  public static Builder builder() {
    return new AutoValue_Sponsor.Builder();
  }

  public static JsonAdapter<Sponsor> jsonAdapter(Moshi moshi) {
    return new AutoValue_Sponsor.MoshiJsonAdapter(moshi);
  }

  Sponsor() {}

  public abstract int id();

  public abstract String name();

  public abstract String link();

  public abstract String photo();

  public abstract List<SponsorLevel> sponsor_levels();

  @Override public int compareTo(Sponsor another) {
    return new SponsorComparator().compare(this, another);
  }

  @AutoValue.Builder public abstract static class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder link(String link);

    public abstract Builder photo(String photo);

    public abstract Builder sponsor_levels(List<SponsorLevel> sponsor_levels);

    public abstract Sponsor build();
  }
}
