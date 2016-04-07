package org.selfconference.android.feedback;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.App;
import org.selfconference.android.api.Api;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SessionPreferences;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An IntentService used to submit feedback for a session
 * <p/>
 * To improve the user experience, the {@link FeedbackFragment} starts this background service
 * to submit the feedback request to the Self.conference API.
 * This allows the {@link FeedbackFragment} dialog to be dismissed while this API call is still in
 * progress.
 * <p/>
 * If there is a successful POST to {@code /sessions/{sessionId}/feedbacks}, this service will post
 * a
 * {@link SuccessfulFeedbackSubmission} event to broadcast the successful completion of this call
 * to
 * anyone listening on the UI thread, namely {@link SessionDetailsActivity}.
 */
public final class SubmitFeedbackIntentService extends IntentService {
  private static final String KEY_SESSION = "session";
  private static final String KEY_FEEDBACK = "feedback";

  @Inject Api api;
  @Inject SessionPreferences preferences;
  @Inject EventBus eventBus;

  public SubmitFeedbackIntentService() {
    super(SubmitFeedbackIntentService.class.getSimpleName());
  }

  @Override public void onCreate() {
    super.onCreate();
    App.getInstance().inject(this);
  }

  /**
   * A factory method to create an {@link Intent} to start the {@link SubmitFeedbackIntentService}
   *
   * @param session the session associated with this feedback. Cannot be {@code null}.
   * @param feedback the feedback associated with this session. Cannot be {@code null}.
   * @return an Intent used to start the {@link SubmitFeedbackIntentService}
   */
  public static Intent newIntent(@NonNull Session session, @NonNull Feedback feedback) {
    checkNotNull(session, "session == null");
    checkNotNull(feedback, "feedback == null");
    return new Intent(App.getInstance(), SubmitFeedbackIntentService.class) //
        .putExtra(KEY_SESSION, session) //
        .putExtra(KEY_FEEDBACK, feedback);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Session session = intent.getParcelableExtra(KEY_SESSION);
    Feedback feedback = intent.getParcelableExtra(KEY_FEEDBACK);

    checkNotNull(session);
    checkNotNull(feedback);

    Response<ResponseBody> response = api.submitFeedback(session, feedback).toBlocking().single();
    if (response.isSuccessful()) {
      preferences.submitFeedback(session);
      eventBus.post(new SuccessfulFeedbackSubmission());
    }
  }
}
