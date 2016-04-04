package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.session.ReadableDateTimeTypeAdapter;
import org.selfconference.android.speakers.ImmutableListSpeakerTypeAdapter;
import org.selfconference.android.speakers.Speaker;

@AutoValue public abstract class Event implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Event.Builder();
  }

  public abstract int id();

  public abstract String about();

  public abstract String ticketsLink();

  @ParcelAdapter(ReadableDateTimeTypeAdapter.class) public abstract ReadableDateTime startDate();

  @ParcelAdapter(ReadableDateTimeTypeAdapter.class) public abstract ReadableDateTime endDate();

  @ParcelAdapter(ImmutableListSpeakerTypeAdapter.class)
  public abstract ImmutableList<Speaker> organizers();

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder id(int id);

    public abstract Builder about(String about);

    public abstract Builder ticketsLink(String ticketsLink);

    public abstract Builder startDate(ReadableDateTime startDate);

    public abstract Builder endDate(ReadableDateTime endDate);

    public abstract Builder organizers(Iterable<Speaker> organizers);

    public abstract Event build();
  }
}
