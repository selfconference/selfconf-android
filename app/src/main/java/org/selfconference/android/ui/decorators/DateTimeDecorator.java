package org.selfconference.android.ui.decorators;

import android.support.annotation.NonNull;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.api.NullDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

/** Decorates a {@link ReadableDateTime} to provide date time view display methods. */
public abstract class DateTimeDecorator {

  @NonNull final ReadableDateTime dateTime;

  /**
   * Returns a concrete implementation of a {@link DateTimeDecorator}.
   *
   * <p>This factory method is the entry point for the class.
   * Only implementations should call the {@link #DateTimeDecorator(ReadableDateTime)} constructor.
   *
   * @param dateTime a {@link ReadableDateTime} to decorate.
   * May be a {@link NullDateTime} or {@link org.joda.time.DateTime}.
   * Must not be null.
   */
  public static DateTimeDecorator fromDateTime(@NonNull ReadableDateTime dateTime) {
    checkNotNull(dateTime, "DateTimeDecorator does not accept a null DateTime");
    if (dateTime instanceof NullDateTime) {
      return new NullDateTimeDecorator(dateTime);
    }
    return new ValidDateTimeDecorator(dateTime);
  }

  DateTimeDecorator(@NonNull ReadableDateTime dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * Returns a long, detailed {@code String} representation of the date and time
   * for displaying in a view. Must not be null.
   */
  @NonNull public abstract String fullDateString();

  /**
   * Returns a short, concise {@link String} representation of the time
   * for displaying in a view. Must not be null.
   */
  @NonNull public abstract String shortTimeString();

  /**
   * Returns a short, concise {@link String} representation of the date
   * for displaying in a view. Must not be null.
   */
  @NonNull public abstract String shortDateString();

  /** A {@link DateTimeDecorator} implementation for a {@link org.joda.time.DateTime}. */
  static final class ValidDateTimeDecorator extends DateTimeDecorator {

    ValidDateTimeDecorator(ReadableDateTime dateTime) {
      super(dateTime);
    }

    @NonNull @Override public String fullDateString() {
      return dateTime.toString("MMM d '/' h:mm a");
    }

    @NonNull @Override public String shortTimeString() {
      String dateString = dateTime.toString("ha");
      return dateString.substring(0, dateString.length() - 1);
    }

    @NonNull @Override public String shortDateString() {
      return dateTime.toString("MMM d");
    }
  }

  /** A {@link DateTimeDecorator} implementation for a {@link NullDateTime}. */
  static final class NullDateTimeDecorator extends DateTimeDecorator {

    private static final String NULL_DATE_STRING = "TBD";

    NullDateTimeDecorator(ReadableDateTime dateTime) {
      super(dateTime);
    }

    @NonNull @Override public String fullDateString() {
      return NULL_DATE_STRING;
    }

    @NonNull @Override public String shortTimeString() {
      return NULL_DATE_STRING;
    }

    @NonNull @Override public String shortDateString() {
      return NULL_DATE_STRING;
    }
  }
}
