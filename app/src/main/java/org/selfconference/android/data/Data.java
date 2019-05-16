package org.selfconference.android.data;

import androidx.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Data<T> {

  public static <T> Builder<T> builder() {
    return new AutoValue_Data.Builder<>();
  }

  public abstract Status status();

  public abstract T data();

  @Nullable public abstract Throwable throwable();

  public abstract Builder<T> toBuilder();

  @AutoValue.Builder public static abstract class Builder<T> {
    public abstract Builder<T> status(Status status);

    public abstract Builder<T> data(T data);

    public abstract Builder<T> throwable(Throwable throwable);

    public abstract Data<T> build();
  }

  public enum Status {
    NONE,
    LOADING,
    LOADED,
    ERROR
  }
}
