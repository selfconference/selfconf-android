package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.event.GetSessionAddEvent;
import org.selfconference.android.data.event.GetSessionSuccessEvent;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public final class GetSessionJob extends ApiJob<Session> {

  private final int id;

  public GetSessionJob(int id) {
    super();
    this.id = id;
  }

  @NonNull @Override protected Object createAddEvent() {
    return new GetSessionAddEvent();
  }

  @Override protected Observable<Result<Session>> apiCall() {
    return restClient.getSessionById(id);
  }

  @Override protected void onApiSuccess(Response<Session> response) {
    Session session = response.body();
    eventBus.post(new GetSessionSuccessEvent(session));
  }

  @Override protected void onApiFailure(Throwable error) {

  }
}
