package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.event.GetSpeakersAddEvent;
import org.selfconference.android.data.event.GetSpeakersSuccessEvent;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSpeakersJob extends ApiJob<List<Speaker>> {

  @NonNull @Override protected Object createAddEvent() {
    return new GetSpeakersAddEvent();
  }

  @Override protected Call<List<Speaker>> apiCall() {
    return restClient.getSpeakers();
  }

  @Override protected void onApiSuccess(Response<List<Speaker>> response) {
    eventBus.post(new GetSpeakersSuccessEvent(response.body()));
  }

  @Override protected void onApiFailure(Response<List<Speaker>> response) {

  }
}
