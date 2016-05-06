package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.List;
import org.threeten.bp.Instant;

@AutoValue public abstract class Event implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Event.Builder();
  }

  public abstract int id();

  public abstract String about();

  public abstract String tickets_link();

  public abstract Instant start_date();

  public abstract Instant end_date();

  public abstract List<Organizer> organizers();

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder about(String about);

    public abstract Builder tickets_link(String tickets_link);

    public abstract Builder start_date(Instant start_date);

    public abstract Builder end_date(Instant end_date);

    public abstract Builder organizers(List<Organizer> organizers);

    public abstract Event build();
  }
}
