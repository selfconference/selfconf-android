package org.selfconference.android.support.file;

import java.io.Reader;

public final class Rooms {

  public static Reader room() {
    return Files.loadFile("rooms/room.json");
  }

  private Rooms() {
    throw new AssertionError("No instances.");
  }
}
