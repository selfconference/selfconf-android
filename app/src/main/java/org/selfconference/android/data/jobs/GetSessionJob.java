package org.selfconference.android.data.jobs;

import android.support.annotation.NonNull;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.events.GetSessionAddEvent;
import org.selfconference.android.data.events.GetSessionSuccessEvent;
import org.selfconference.android.session.Session;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSessionJob extends ApiJob<Session> {

  private final int id;

  public GetSessionJob(int id) {
    super();
    this.id = id;
  }

  @NonNull @Override protected Object createAddEvent() {
    return new GetSessionAddEvent();
  }

  @Override protected Call<Session> apiCall() {
    return api.getSessionById(id);
  }

  @Override protected void onApiSuccess(Response<Session> response) {
    Session session = response.body();
    eventBus.post(new GetSessionSuccessEvent(session));
  }

  @Override protected void onApiFailure(Response<Session> response) {

  }
}
