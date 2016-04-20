package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import java.util.List;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.parcel.ReadableDateTimeTypeAdapter;

@AutoValue public abstract class Event implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Event.Builder();
  }

  public abstract int id();

  public abstract String about();

  public abstract String ticketsLink();

  @ParcelAdapter(ReadableDateTimeTypeAdapter.class)
  public abstract ReadableDateTime startDate();

  @ParcelAdapter(ReadableDateTimeTypeAdapter.class)
  public abstract ReadableDateTime endDate();

  // @ParcelAdapter(Organizer.ImmutableListTypeAdapter.class)
  public abstract List<Organizer> organizers();

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder about(String about);

    public abstract Builder ticketsLink(String ticketsLink);

    public abstract Builder startDate(ReadableDateTime startDate);

    public abstract Builder endDate(ReadableDateTime endDate);

    public abstract Builder organizers(List<Organizer> organizers);

    public abstract Event build();
  }
}
