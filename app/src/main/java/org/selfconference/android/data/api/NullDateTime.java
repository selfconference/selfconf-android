package org.selfconference.android.data.api;

import org.joda.time.Chronology;
import org.joda.time.base.AbstractDateTime;
import org.joda.time.chrono.ISOChronology;
import org.selfconference.android.utils.DateTimes;

/** A Null Object implementation that represents {@link org.joda.time.DateTime}. */
public final class NullDateTime extends AbstractDateTime {
  /**
   * An arbitrary negative value in milliseconds to differentiate between a valid
   * {@link org.joda.time.ReadableDateTime} and the Null Object {@link NullDateTime}.
   * Valid {@code DateTime}s should have a value greater than zero for the number of milliseconds
   * since the epoch.
   */
  public static final long MILLIS = -10000;

  /** A factory method that returns a {@link NullDateTime} instance. */
  public static NullDateTime create() {
    return new NullDateTime();
  }

  private NullDateTime() {
    super();
  }

  @Override public long getMillis() {
    return MILLIS;
  }

  @Override public Chronology getChronology() {
    return ISOChronology.getInstance(DateTimes.EST);
  }
}
