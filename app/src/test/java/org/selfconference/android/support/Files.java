package org.selfconference.android.support;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

final class Files {

  static Reader loadFile(String path) {
    InputStream resourceStream = Files.class.getClassLoader().getResourceAsStream(path);
    return new InputStreamReader(resourceStream);
  }

  private Files() {
    throw new AssertionError("No instances.");
  }
}
