package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Lists;
import java.util.List;

@AutoValue public abstract class Speaker implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Speaker.Builder() //
        .twitter("") //
        .sessions(Lists.<Integer>newArrayList());
  }

  Speaker() {}

  public abstract int id();

  public abstract String name();

  public abstract String twitter();

  public abstract String bio();

  public abstract String photo();

  public abstract List<Integer> sessions();

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder twitter(String twitter);

    public abstract Builder bio(String bio);

    public abstract Builder photo(String photo);

    public abstract Builder sessions(List<Integer> sessions);

    public abstract Speaker build();
  }
}
