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

  public abstract String ticketsLink();

  public abstract Instant startDate();

  public abstract Instant endDate();

  public abstract List<Organizer> organizers();

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder about(String about);

    public abstract Builder ticketsLink(String ticketsLink);

    public abstract Builder startDate(Instant startDate);

    public abstract Builder endDate(Instant endDate);

    public abstract Builder organizers(List<Organizer> organizers);

    public abstract Event build();
  }
}
