package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Lists;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

@AutoValue public abstract class Speaker implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Speaker.Builder() //
        .twitter("") //
        .sessions(Lists.newArrayList());
  }

  public static JsonAdapter<Speaker> jsonAdapter(Moshi moshi) {
    return new AutoValue_Speaker.MoshiJsonAdapter(moshi);
  }

  Speaker() {}

  public abstract int id();

  public abstract String name();

  public abstract String twitter();

  public abstract String bio();

  public abstract String headshot();

  @Nullable public abstract List<Session> sessions();

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder twitter(String twitter);

    public abstract Builder bio(String bio);

    public abstract Builder headshot(String photo);

    public abstract Builder sessions(List<Session> sessions);

    public abstract Speaker build();
  }
}
