package org.selfconference.support.file;

import java.io.Reader;

public final class Sponsors {

  public static Reader withMultipleSponsorLevels() {
    return Files.loadFile("sponsors/with-multiple-sponsor-levels.json");
  }

  private Sponsors() {
    throw new AssertionError("No instances.");
  }
}
