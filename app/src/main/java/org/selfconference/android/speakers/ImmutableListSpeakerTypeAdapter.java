package org.selfconference.android.speakers;

import android.os.Parcel;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;

public final class ImmutableListSpeakerTypeAdapter implements TypeAdapter<ImmutableList<Speaker>> {
  @Override public ImmutableList<Speaker> fromParcel(Parcel in) {
    List<AutoValue_Speaker> typedArrayList = in.createTypedArrayList(AutoValue_Speaker.CREATOR);
    return ImmutableList.copyOf(typedArrayList);
  }

  @Override public void toParcel(ImmutableList<Speaker> value, Parcel dest) {
    dest.writeTypedList(value);
  }
}
