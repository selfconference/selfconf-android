package org.selfconference.android.ui.session;

import org.selfconference.android.util.Instants;
import org.threeten.bp.Instant;

public enum Day {
  ONE(Instants.fromEstString("2016-05-20T00:00:00.000-05:00")),
  TWO(Instants.fromEstString("2016-05-21T00:00:00.000-05:00"));

  private final Instant startTime;

  Day(Instant startTime) {
    this.startTime = startTime;
  }

  public Instant getStartTime() {
    return startTime;
  }

  public static Day fromPosition(int position) {
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
