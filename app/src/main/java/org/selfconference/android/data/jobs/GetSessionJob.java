package org.selfconference.android.data.jobs;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.events.GetSessionAddEvent;
import org.selfconference.android.data.events.GetSessionSuccessEvent;
import org.selfconference.android.session.Session;
import retrofit2.Response;

public final class GetSessionJob extends Job {

  @Inject Api api;
  @Inject EventBus eventBus;

  private final int id;

  public static Job create(int id) {
    return new GetSessionJob(id);
  }

  private GetSessionJob(int id) {
    super(new Params(Priorities.DEFAULT).requireNetwork());
    this.id = id;
  }

  @Override public void onAdded() {
    eventBus.post(new GetSessionAddEvent());
  }

  @Override public void onRun() throws Throwable {
    Response<Session> response = api.getSessionById(id).execute();
    if (response.isSuccessful()) {
      eventBus.post(new GetSessionSuccessEvent(response.body()));
    } else {
      // TODO handle error
    }
  }

  @Override protected void onCancel(int cancelReason) {

  }

  @Override protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
      int maxRunCount) {
    return RetryConstraint.createExponentialBackoff(runCount, 1000);
  }
}
