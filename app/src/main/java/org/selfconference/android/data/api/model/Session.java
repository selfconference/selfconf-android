package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.google.common.collect.Lists;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import java.util.List;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.parcel.ReadableDateTimeTypeAdapter;

@AutoValue public abstract class Session implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Session.Builder() //
        .speakers(Lists.<Speaker>newArrayList());
  }

  Session() {}

  public abstract int id();

  public abstract String title();

  @NonNull public abstract Room room();

  public abstract String description();

  public abstract boolean keynote();

  @NonNull @ParcelAdapter(ReadableDateTimeTypeAdapter.class)
  public abstract ReadableDateTime beginning();

  public abstract List<Speaker> speakers();

  @AutoValue.Builder public static abstract class Builder {

    public abstract Builder id(int id);

    public abstract Builder title(String title);

    public abstract Builder room(Room room);

    public abstract Builder description(String description);

    public abstract Builder keynote(boolean keynote);

    public abstract Builder beginning(ReadableDateTime beginning);

    public abstract Builder speakers(List<Speaker> speakers);

    public abstract Session build();
  }
}
