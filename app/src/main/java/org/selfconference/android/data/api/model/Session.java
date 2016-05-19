package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

@AutoValue public abstract class Session implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Session.Builder() //
        .speakers(Lists.<Speaker>newArrayList());
  }

  public static JsonAdapter<Session> jsonAdapter(Moshi moshi) {
    return new AutoValue_Session.MoshiJsonAdapter(moshi);
  }

  Session() {}

  public abstract int id();

  public abstract String name();

  @Nullable public abstract Room room();

  @Json(name = "abstract") @Nullable public abstract String description();

  public abstract boolean keynote();

  @Nullable public abstract Slot slot();

  @Nullable public abstract List<Speaker> speakers();

  @Override public String toString() {
    return MoreObjects.toStringHelper(this) //
        .add("id", id()) //
        .add("name", name()) //
        .toString();
  }

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder room(Room room);

    public abstract Builder description(String description);

    public abstract Builder keynote(boolean keynote);

    public abstract Builder slot(Slot slot);

    public abstract Builder speakers(List<Speaker> speakers);

    public abstract Session build();
  }
}
