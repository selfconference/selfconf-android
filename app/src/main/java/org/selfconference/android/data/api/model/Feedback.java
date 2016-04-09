package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Feedback implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Feedback.Builder();
  }

  public abstract Vote vote();

  @NonNull public abstract String comments();

  @AutoValue.Builder public abstract static class Builder {
    public abstract Builder vote(Vote vote);

    public abstract Builder comments(String comments);

    public abstract Feedback build();
  }
}
