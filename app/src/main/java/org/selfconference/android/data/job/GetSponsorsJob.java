package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.SponsorComparator;
import org.selfconference.android.data.event.GetSponsorsAddEvent;
import org.selfconference.android.data.event.GetSponsorsSuccessEvent;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSponsorsJob extends ApiJob<List<Sponsor>> {

  @NonNull @Override protected Object createAddEvent() {
    return new GetSponsorsAddEvent();
  }

  @Override protected Call<List<Sponsor>> apiCall() {
    return api.getSponsors();
  }

  @Override protected void onApiSuccess(Response<List<Sponsor>> response) {
    List<Sponsor> sponsors = response.body();
    ImmutableList<Sponsor> sortedSponsors = Ordering.from(new SponsorComparator()) //
        .immutableSortedCopy(sponsors);
    eventBus.post(new GetSponsorsSuccessEvent(sortedSponsors));
  }

  @Override protected void onApiFailure(Response<List<Sponsor>> response) {

  }
}
