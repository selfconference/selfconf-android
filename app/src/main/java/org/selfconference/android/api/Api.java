package org.selfconference.android.api;

import java.util.List;
import okhttp3.ResponseBody;
import org.selfconference.android.feedback.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;

/**
 * A wrapper around the Self.conference API
 * <p/>
 * Current implementations include {@link SelfConferenceApi}
 */
public interface Api {
  Call<List<Session>> getSessions();

  Call<Session> getSessionById(int id);

  Call<List<Speaker>> getSpeakers();

  Call<List<Sponsor>> getSponsors();

  /**
   * A POST request to submit feedback for a session
   *
   * @return an observable response to determine success or failure of call
   */
  Call<ResponseBody> submitFeedback(Session session, Feedback feedback);
}
