package org.selfconference.data.api.model;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.threeten.bp.Instant;

@AutoValue public abstract class Slot implements Parcelable, Comparable<Slot> {

  public static Slot empty() {
    return new AutoValue_Slot(-1, Instant.MIN);
  }

  public static Builder builder() {
    return new AutoValue_Slot.Builder();
  }

  public static JsonAdapter<Slot> jsonAdapter(Moshi moshi) {
    return new AutoValue_Slot.MoshiJsonAdapter(moshi);
  }

  public abstract int id();

  public abstract Instant time();

  @Override public int compareTo(@NonNull Slot another) {
    return this.time().compareTo(another.time());
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder time(Instant time);

    public abstract Slot build();
  }
}
