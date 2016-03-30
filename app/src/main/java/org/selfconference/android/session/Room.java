package org.selfconference.android.session;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Room implements Parcelable {

  /** Returns a Null Object to represent missing room data. */
  @NonNull public static Room nullRoom() {
    return create(-1, "TBD");
  }

  /** A factory method that returns a {@code Room} instance. */
  @NonNull public static Room create(int id, String name) {
    return new AutoValue_Room(id, name);
  }

  /** A no-args constructor used by {@link com.google.gson.Gson}. */
  Room() {}

  /** Returns the room's database ID. */
  public abstract int id();

  /** Returns the room's human-readable name. */
  public abstract String name();
}
