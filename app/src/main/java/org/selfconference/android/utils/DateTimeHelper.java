package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.selfconference.android.session.Day;

public final class DateTimeHelper {
  private static final DateTimeZone EST = DateTimeZone.forOffsetHours(-5);
  private static final DateTimeFormatter DEFAULT_FORMATTER =
      ISODateTimeFormat.dateOptionalTimeParser().withZone(EST);

  public static DateTime parseDateTime(String time) {
    return DateTime.parse(time, DEFAULT_FORMATTER);
  }

  public static Interval intervalForDay(Day day) {
    DateTime startTime = day.getStartTime();
    DateTime end = startTime.plusDays(1).minusSeconds(1);
    return new Interval(startTime, end);
  }

  private DateTimeHelper() {
    throw new AssertionError("No instances.");
  }
}
