package org.selfconference.android.session;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class Room implements Parcelable {

  public static Room emptyRoom() {
    return create(-1, "");
  }

  public static Room create(int id, String name) {
    return new AutoValue_Room(id, name);
  }

  Room() {}

  public abstract int id();

  public abstract String name();
}
