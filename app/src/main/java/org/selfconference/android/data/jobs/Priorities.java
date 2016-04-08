package org.selfconference.android.data.jobs;

/** Represents the priority of a {@link com.birbit.android.jobqueue.Job}. */
public final class Priorities {

  public static final int LOW = 1;
  public static final int NORMAL = 10;
  public static final int HIGH = 100;

  private Priorities() {
    throw new AssertionError("No instances.");
  }
}
