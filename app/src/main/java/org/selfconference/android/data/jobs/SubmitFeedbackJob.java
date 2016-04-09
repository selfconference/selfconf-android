package org.selfconference.android.data.jobs;

import android.support.annotation.NonNull;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.events.SubmitFeedbackAddEvent;
import org.selfconference.android.data.events.SubmitFeedbackSuccessEvent;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.session.SessionPreferences;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SubmitFeedbackJob extends ApiJob<ResponseBody> {

  @Inject SessionPreferences preferences;

  private final Session session;
  private final Feedback feedback;

  public SubmitFeedbackJob(@NonNull Session session, @NonNull Feedback feedback) {
    super();
    this.session = checkNotNull(session, "session == null");
    this.feedback = checkNotNull(feedback, "feedback == null");
  }

  @NonNull @Override protected Object createAddEvent() {
    return new SubmitFeedbackAddEvent();
  }

  @Override protected Call<ResponseBody> apiCall() {
    return api.submitFeedback(session, feedback);
  }

  @Override protected void onApiSuccess(Response<ResponseBody> response) {
    preferences.submitFeedback(session);
    eventBus.post(new SubmitFeedbackSuccessEvent());
  }

  @Override protected void onApiFailure(Response<ResponseBody> response) {

  }
}
