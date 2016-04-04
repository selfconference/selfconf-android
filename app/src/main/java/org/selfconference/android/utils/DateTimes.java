package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/** Static utility methods pertaining to {@link DateTime} instances. */
public final class DateTimes {
  public static final DateTimeZone EST = DateTimeZone.forOffsetHours(-5);

  private static final DateTimeFormatter DEFAULT_FORMATTER =
      ISODateTimeFormat.dateOptionalTimeParser().withZone(EST);

  /**
   * Parses a date time string using a UTC-5:00 offset {@link DateTimeFormatter}.
   *
   * <p>This method wraps {@link DateTime#parse(String)} to ensure date time strings are parsed
   * taking EST date time strings into consideration, since the API will return strings
   * with a UTC-5:00 offset.
   *
   * <p>When parsing date times from the API, this method should always be favored.
   *
   * @param dateTimeString the date time String returned from the API.
   * @return a {@link DateTime} parsed using an EST {@link DateTimeFormatter}.
   */
  public static DateTime parseEst(String dateTimeString) {
    return DateTime.parse(dateTimeString, DEFAULT_FORMATTER);
  }

  private DateTimes() {
    throw new AssertionError("No instances.");
  }
}
