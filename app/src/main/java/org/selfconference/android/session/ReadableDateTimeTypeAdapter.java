package org.selfconference.android.session;

import android.os.Parcel;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.api.NullDateTime;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ReadableDateTimeTypeAdapter implements TypeAdapter<ReadableDateTime> {
  @Override public ReadableDateTime fromParcel(Parcel in) {
    long instant = in.readLong();
    if (instant == NullDateTime.MILLIS) {
      return NullDateTime.create();
    }
    return new DateTime(instant);
  }

  @Override public void toParcel(ReadableDateTime value, Parcel dest) {
    checkNotNull(value, "DateTime == null");
    dest.writeLong(value.getMillis());
  }
}
