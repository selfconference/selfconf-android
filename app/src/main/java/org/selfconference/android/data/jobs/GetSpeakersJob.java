package org.selfconference.android.data.jobs;

import android.support.annotation.NonNull;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.events.GetSpeakersAddEvent;
import org.selfconference.android.data.events.GetSpeakersSuccessEvent;
import org.selfconference.android.data.api.model.Speaker;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSpeakersJob extends ApiJob<List<Speaker>> {

  @NonNull @Override protected Object createAddEvent() {
    return new GetSpeakersAddEvent();
  }

  @Override protected Call<List<Speaker>> apiCall() {
    return api.getSpeakers();
  }

  @Override protected void onApiSuccess(Response<List<Speaker>> response) {
    ImmutableList<Speaker> speakers = ImmutableList.copyOf(response.body());
    eventBus.post(new GetSpeakersSuccessEvent(speakers));
  }

  @Override protected void onApiFailure(Response<List<Speaker>> response) {

  }
}
