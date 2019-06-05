package org.selfconference.android.data.api.model;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue public abstract class Room implements Parcelable {

  /** Returns a Null Object to represent missing room data. */
  @NonNull public static Room empty() {
    return create(-1, "TBD");
  }

  /** A factory method that returns a {@code Room} instance. */
  @NonNull public static Room create(int id, String name) {
    return new AutoValue_Room(id, name);
  }

  public static JsonAdapter<Room> jsonAdapter(Moshi moshi) {
    return new AutoValue_Room.MoshiJsonAdapter(moshi);
  }

  Room() {}

  /** Returns the room's database ID. */
  public abstract int id();

  /** Returns the room's human-readable name. */
  public abstract String name();
}
