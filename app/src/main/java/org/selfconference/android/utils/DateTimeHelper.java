package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.selfconference.android.api.Day;

public class DateTimeHelper {
    public static final DateTimeFormatter JSON_PATTERN = DateTimeFormat.forPattern("MM/dd/YY HH:mm:ss");
    public static final DateTimeZone EST = DateTimeZone.forOffsetHours(-5);

    public static DateTime parseDateTime(String time) {
        return DateTime.parse(time, JSON_PATTERN);
    }

    public static Interval intervalForDay(Day day) {
        int d = day == Day.ONE ? 30 : 31;
        final DateTime start = new DateTime(2014, 5, d, 0, 0, 0).withZone(EST);
        final DateTime end = new DateTime(2014, 5, d, 23, 59, 59).withZone(EST);
        return new Interval(start, end);
    }
}
