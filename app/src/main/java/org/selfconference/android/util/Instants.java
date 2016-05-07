package org.selfconference.android.util;

import java.util.Locale;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoUnit;

public final class Instants {

  private static final ZoneOffset EST_OFFSET = ZoneOffset.ofHours(-5);

  public static Instant fromEstString(String text) {
    return ZonedDateTime.parse(text).toInstant();
  }

  public static boolean areOnSameDay(Instant left, Instant right) {
    Instant leftTime = left.truncatedTo(ChronoUnit.DAYS);
    Instant rightTime = right.truncatedTo(ChronoUnit.DAYS);
    return leftTime.compareTo(rightTime) == 0;
  }

  public static String miniTimeString(Instant instant) {
    if (Instant.MIN.equals(instant)) {
      return "TBD";
    }
    OffsetDateTime offsetDateTime = offset(instant);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ha");
    return dateTimeFormatter.format(offsetDateTime);
  }

  public static String shortTimeString(Instant instant) {
    if (Instant.MIN.equals(instant)) {
      return "TBD";
    }
    OffsetDateTime offsetDateTime = offset(instant);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    return dateTimeFormatter.format(offsetDateTime);
  }

  public static String monthDayString(Instant instant) {
    if (Instant.MIN.equals(instant)) {
      return "TBD";
    }
    OffsetDateTime offsetDateTime = offset(instant);
    String month = offsetDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
    int day = offsetDateTime.getDayOfMonth();
    return String.format("%s %s", month, day);
  }

  private static OffsetDateTime offset(Instant instant) {
    return instant.atOffset(EST_OFFSET);
  }

  private Instants() {
    throw new AssertionError("No instances.");
  }
}
