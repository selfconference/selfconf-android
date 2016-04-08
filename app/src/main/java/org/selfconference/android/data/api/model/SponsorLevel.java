package org.selfconference.android.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;

@AutoValue public abstract class SponsorLevel implements Parcelable, Comparable<SponsorLevel> {

  public static Builder builder() {
    return new AutoValue_SponsorLevel.Builder();
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

  public static final class ImmutableListTypeAdapter
      implements TypeAdapter<ImmutableList<SponsorLevel>> {

    @Override public ImmutableList<SponsorLevel> fromParcel(Parcel in) {
      List<AutoValue_SponsorLevel> typedArrayList =
          in.createTypedArrayList(AutoValue_SponsorLevel.CREATOR);
      return ImmutableList.copyOf(typedArrayList);
    }

    @Override public void toParcel(ImmutableList<SponsorLevel> value, Parcel dest) {
      dest.writeTypedList(value);
    }
  }
}
