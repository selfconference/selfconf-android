package org.selfconference.android.data.jobs;

public final class Priorities {

  public static final int HIGH = 100;
  public static final int MEDIUM = 10;
  public static final int LOW = 1;
  public static final int DEFAULT = MEDIUM;

  private Priorities() {
    throw new AssertionError("No instances.");
  }
}
