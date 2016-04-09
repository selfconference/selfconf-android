package org.selfconference.android.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.parcel.ReadableDateTimeTypeAdapter;

@AutoValue public abstract class Session implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Session.Builder();
  }

  Session() {
  }

  public abstract int id();

  public abstract String title();

  @NonNull public abstract Room room();

  public abstract String description();

  public abstract boolean keynote();

  @NonNull @ParcelAdapter(ReadableDateTimeTypeAdapter.class)
  public abstract ReadableDateTime beginning();

  @ParcelAdapter(Speaker.ImmutableListTypeAdapter.class)
  public abstract ImmutableList<Speaker> speakers();

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder title(String title);

    public abstract Builder room(Room room);

    public abstract Builder description(String description);

    public abstract Builder keynote(boolean keynote);

    public abstract Builder beginning(ReadableDateTime beginning);

    public abstract Builder speakers(ImmutableList<Speaker> speakers);

    public abstract Session build();
  }

  public static final class ImmutableListTypeAdapter
      implements TypeAdapter<ImmutableList<Session>> {
    @Override public ImmutableList<Session> fromParcel(Parcel in) {
      List<AutoValue_Session> typedArrayList = in.createTypedArrayList(AutoValue_Session.CREATOR);
      return ImmutableList.copyOf(typedArrayList);
    }

    @Override public void toParcel(ImmutableList<Session> value, Parcel dest) {
      dest.writeTypedList(value);
    }
  }
}
