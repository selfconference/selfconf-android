package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.selfconference.android.R;

import static org.selfconference.android.utils.ResourceProvider.getString;

public final class DateStringer {

  public static String toDateString(DateTime dateTime) {
    DateTime now = DateTime.now();

    LocalDate today = now.toLocalDate();
    LocalDate tomorrow = today.plusDays(1);
    LocalDate twoDaysAway = today.plusDays(2);

    DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
    DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay(now.getZone());
    DateTime startOfTwoDaysAway = twoDaysAway.toDateTimeAtStartOfDay(now.getZone());

    StringBuilder stringBuilder = new StringBuilder();

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
    String timeString = dateTime.toString("ha");
    return timeString.substring(0, timeString.length() - 1);
  }

  private DateStringer() {
    throw new AssertionError("No instances.");
  }
}
