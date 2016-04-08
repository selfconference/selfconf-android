package org.selfconference.android.data.jobs;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.events.GetSpeakersAddEvent;
import org.selfconference.android.data.events.GetSpeakersSuccessEvent;
import org.selfconference.android.speakers.Speaker;
import retrofit2.Response;

public final class GetSpeakersJob extends Job {

  @Inject Api api;
  @Inject EventBus eventBus;

  public static Job create() {
    return new GetSpeakersJob();
  }

  private GetSpeakersJob() {
    super(new Params(Priorities.DEFAULT).requireNetwork());
  }

  @Override public void onAdded() {
    eventBus.post(new GetSpeakersAddEvent());
  }

  @Override public void onRun() throws Throwable {
    Response<List<Speaker>> response = api.getSpeakers().execute();
    if (response.isSuccessful()) {
      ImmutableList<Speaker> speakers = ImmutableList.copyOf(response.body());
      eventBus.post(new GetSpeakersSuccessEvent(speakers));
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
