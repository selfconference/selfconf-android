package org.selfconference.android.support.file;

import java.io.Reader;

public final class Sessions {

  public static Reader withoutBeginning() {
    return Files.loadFile("sessions/without-beginning.json");
  }

  public static Reader withoutRoom() {
    return Files.loadFile("sessions/without-room.json");
  }

  public static Reader withNullKeynote() {
    return Files.loadFile("sessions/with-null-keynote.json");
  }

  public static Reader withoutKeynote() {
    return Files.loadFile("sessions/without-keynote.json");
  }

  private Sessions() {
    throw new AssertionError("No instances.");
  }
}
