package org.selfconference.android.data.api;

import org.selfconference.android.data.api.model.Room;

final class MockRooms {

  static final Room EARTH = Room.create(1, "Earth");
  static final Room MARS = Room.create(2, "Mars");
  static final Room VENUS = Room.create(3, "Venus");
  static final Room JUPITER = Room.create(4, "Jupiter");
  static final Room MERCURY = Room.create(5, "Mercury");

  private MockRooms() {
    throw new AssertionError("No instances.");
  }
}
