package org.selfconference.android.session;

import android.os.Parcel;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import org.joda.time.DateTime;

public final class DateTimeTypeAdapter implements TypeAdapter<DateTime> {
  @Override public DateTime fromParcel(Parcel in) {
    Object readValue = in.readValue(DateTime.class.getClassLoader());
    return readValue == null ? null : new DateTime((long) readValue);
  }

  @Override public void toParcel(DateTime value, Parcel dest) {
    Long dateTimeValue = value == null ? null : value.getMillis();
    dest.writeValue(dateTimeValue);
  }
}
