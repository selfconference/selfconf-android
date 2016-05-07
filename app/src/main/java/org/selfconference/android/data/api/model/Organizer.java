package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue public abstract class Organizer implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Organizer.Builder();
  }

  public static JsonAdapter<Organizer> jsonAdapter(Moshi moshi) {
    return new AutoValue_Organizer.MoshiJsonAdapter(moshi);
  }

  Organizer() {}

  public abstract int id();

  public abstract String name();

  public abstract String bio();

  public abstract String email();

  public abstract String twitter();

  public abstract String photo();

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder bio(String bio);

    public abstract Builder email(String email);

    public abstract Builder twitter(String twitter);

    public abstract Builder photo(String photo);

    public abstract Organizer build();
  }
}
