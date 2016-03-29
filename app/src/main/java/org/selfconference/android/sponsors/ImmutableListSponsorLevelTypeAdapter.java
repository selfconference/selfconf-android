package org.selfconference.android.sponsors;

import android.os.Parcel;
import com.google.common.collect.ImmutableList;
import com.ryanharter.auto.value.parcel.TypeAdapter;
import java.util.List;

public final class ImmutableListSponsorLevelTypeAdapter
    implements TypeAdapter<ImmutableList<SponsorLevel>> {

  @Override public ImmutableList<SponsorLevel> fromParcel(Parcel in) {
    List<AutoValue_SponsorLevel> typedArrayList =
        in.createTypedArrayList(AutoValue_SponsorLevel.CREATOR);
    return ImmutableList.copyOf(typedArrayList);
  }

  @Override public void toParcel(ImmutableList<SponsorLevel> value, Parcel dest) {
    dest.writeTypedList(value);
  }
}
