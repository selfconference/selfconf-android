package org.selfconference.android.session;

import android.os.Parcel;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;

public final class ImmutableListSessionTypeAdapter implements TypeAdapter<ImmutableList<Session>> {
  @Override public ImmutableList<Session> fromParcel(Parcel in) {
    List<AutoValue_Session> typedArrayList = in.createTypedArrayList(AutoValue_Session.CREATOR);
    return ImmutableList.copyOf(typedArrayList);
  }

  @Override public void toParcel(ImmutableList<Session> value, Parcel dest) {
    dest.writeTypedList(value);
  }
}
