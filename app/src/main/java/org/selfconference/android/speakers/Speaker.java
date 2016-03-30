package org.selfconference.android.speakers;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.ParcelAdapter;
import java.util.List;
import org.selfconference.android.session.ImmutableListSessionTypeAdapter;
import org.selfconference.android.session.Session;

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

  @ParcelAdapter(ImmutableListSessionTypeAdapter.class)
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
}
