package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.selfconference.android.R;

import static org.selfconference.android.utils.ResourceProvider.getString;

public final class DateStringer {

  public static String toDateString(DateTime dateTime) {
    final DateTime now = DateTime.now();

    final LocalDate today = now.toLocalDate();
    final LocalDate tomorrow = today.plusDays(1);
    final LocalDate twoDaysAway = today.plusDays(2);

    final DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
    final DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
    final DateTime startOfTwoDaysAway = twoDaysAway.toDateTimeAtStartOfDay(now.getZone());

    final StringBuilder stringBuilder = new StringBuilder();

    if (dateTime.isAfter(startOfToday) && dateTime.isBefore(startOfTomorrow)) {
      stringBuilder.append(getString(R.string.today));
    } else if (dateTime.isAfter(startOfTomorrow) && dateTime.isBefore(startOfTwoDaysAway)) {
      stringBuilder.append(getString(R.string.tomorrow));
    } else {
      stringBuilder.append(dateTime.toString("M/d/Y"));
    }

    return stringBuilder.append(" at ") //
        .append(dateTime.toString("h:mm a")) //
        .toString();
  }

  public static String toShortDateString(DateTime dateTime) {
    final String timeString = dateTime.toString("ha");
    return timeString.substring(0, timeString.length() - 1);
  }

  private DateStringer() {
    throw new AssertionError("No instances.");
  }
}
