package org.selfconference.support.file;

import java.io.Reader;

public final class Events {

  public static Reader eventJson() {
    return Files.loadFile("events/event.json");
  }

  private Events() {
    throw new AssertionError("No instances.");
  }
}
