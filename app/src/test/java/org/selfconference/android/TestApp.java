package org.selfconference.android;

public class TestApp extends App {

  @Override protected void installLeakCanary() {
    // do not install Canary for tests
  }

  @Override protected void setupFabric() {
    // do not initialize Fabric for tests
  }
}
