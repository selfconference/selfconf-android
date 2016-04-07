package org.selfconference.android.data.jobs;

import android.support.annotation.NonNull;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.App;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.events.SubmitFeedbackAddEvent;
import org.selfconference.android.data.events.SubmitFeedbackSuccessEvent;
import org.selfconference.android.feedback.Feedback;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionPreferences;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SubmitFeedbackJob extends Job {

  @Inject Api api;
  @Inject EventBus eventBus;
  @Inject SessionPreferences preferences;

  private final Session session;
  private final Feedback feedback;

  public static SubmitFeedbackJob create(@NonNull Session session, @NonNull Feedback feedback) {
    checkNotNull(session, "session == null");
    checkNotNull(feedback, "feedback == null");
    return new SubmitFeedbackJob(session, feedback);
  }

  private SubmitFeedbackJob(Session session, Feedback feedback) {
    super(new Params(Priorities.DEFAULT).requireNetwork());
    this.session = session;
    this.feedback = feedback;
    App.getInstance().inject(this);
  }

  @Override public void onAdded() {
    eventBus.post(new SubmitFeedbackAddEvent());
  }

  @Override public void onRun() throws Throwable {
    Response<ResponseBody> response = api.submitFeedback(session, feedback).toBlocking().first();
    if (response.isSuccessful()) {
      preferences.submitFeedback(session);
      eventBus.post(new SubmitFeedbackSuccessEvent());
    }
  }

  @Override protected void onCancel(int cancelReason) {

  }

  @Override protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
      int maxRunCount) {
    return RetryConstraint.createExponentialBackoff(runCount, 1000);
  }
}
