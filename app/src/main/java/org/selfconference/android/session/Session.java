package org.selfconference.android.session;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import org.joda.time.DateTime;
import org.selfconference.android.speakers.ImmutableListSpeakerTypeAdapter;
import org.selfconference.android.speakers.Speaker;

@AutoValue public abstract class Session implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Session.Builder();
  }

  Session() {}

  public abstract int id();

  public abstract String title();

  @NonNull public abstract Room room();

  public abstract String description();

  public abstract boolean keynote();

  @Nullable @ParcelAdapter(DateTimeTypeAdapter.class) public abstract DateTime beginning();

  @ParcelAdapter(ImmutableListSpeakerTypeAdapter.class)
  public abstract ImmutableList<Speaker> speakers();

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder title(String title);

    public abstract Builder room(Room room);

    public abstract Builder description(String description);

    public abstract Builder keynote(boolean keynote);

    public abstract Builder beginning(DateTime beginning);

    public abstract Builder speakers(ImmutableList<Speaker> speakers);

    public abstract Session build();
  }
}
