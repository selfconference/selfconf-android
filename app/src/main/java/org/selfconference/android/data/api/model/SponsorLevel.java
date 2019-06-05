package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ComparisonChain;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue public abstract class SponsorLevel implements Parcelable, Comparable<SponsorLevel> {

  public static Builder builder() {
    return new AutoValue_SponsorLevel.Builder();
  }

  public static JsonAdapter<SponsorLevel> jsonAdapter(Moshi moshi) {
    return new AutoValue_SponsorLevel.MoshiJsonAdapter(moshi);
  }

  SponsorLevel() {
  }

  public abstract int id();

  public abstract String name();

  public abstract int order();

  @Override public int compareTo(@NonNull SponsorLevel that) {
    return ComparisonChain.start() //
        .compare(this.order(), that.order()) //
        .result();
  }

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder order(int order);

    public abstract SponsorLevel build();
  }
}
