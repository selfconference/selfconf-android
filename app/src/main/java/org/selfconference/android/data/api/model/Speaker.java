package org.selfconference.android.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;

@AutoValue public abstract class Speaker implements Parcelable {

  public static Builder builder() {
    return new AutoValue_Speaker.Builder();
  }

  Speaker() {}

  public abstract int id();

  public abstract String name();

  public abstract String twitter();

  public abstract String bio();

  public abstract String photo();

  @ParcelAdapter(Session.ImmutableListTypeAdapter.class)
  public abstract ImmutableList<Session> sessions();

  @AutoValue.Builder public abstract static class Builder {

    public abstract Builder id(int id);

    public abstract Builder name(String name);

    public abstract Builder twitter(String twitter);

    public abstract Builder bio(String bio);

    public abstract Builder photo(String photo);

    abstract ImmutableList.Builder<Session> sessionsBuilder();

    public Builder addSessions(List<Session> sessions) {
      sessionsBuilder().addAll(sessions);
      return this;
    }

    public abstract Speaker build();
  }

  public static final class ImmutableListTypeAdapter
      implements TypeAdapter<ImmutableList<Speaker>> {
    @Override public ImmutableList<Speaker> fromParcel(Parcel in) {
      List<AutoValue_Speaker> typedArrayList = in.createTypedArrayList(AutoValue_Speaker.CREATOR);
      return ImmutableList.copyOf(typedArrayList);
    }

    @Override public void toParcel(ImmutableList<Speaker> value, Parcel dest) {
      dest.writeTypedList(value);
    }
  }
}
