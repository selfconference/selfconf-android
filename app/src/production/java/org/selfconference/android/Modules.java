package org.selfconference.android;

final class Modules {
  static Object[] list(App app) {
    return new Object[] {
        new AppModule(app),
    };
  }

  private Modules() {
    throw new AssertionError("No instances.");
  }
}
