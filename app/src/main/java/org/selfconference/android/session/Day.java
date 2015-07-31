package org.selfconference.android.session;

import org.joda.time.DateTime;

public enum Day {
  ONE(new DateTime(2015, 5, 29, 0, 0, 0)),
  TWO(new DateTime(2015, 5, 30, 0, 0, 0));

  private final DateTime startTime;

  Day(DateTime startTime) {
    this.startTime = startTime;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public static Day fromPosition(final int position) {
    switch (position) {
      case 0:
        return ONE;
      case 1:
        return TWO;
      default:
        throw new IllegalArgumentException("position must be 0 or 1 but position == " + position);
    }
  }
}
