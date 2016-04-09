package org.selfconference.android.data.api;

import java.util.List;
import okhttp3.ResponseBody;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;

/**
 * A wrapper around the Self.conference API
 * <p/>
 * Current implementations include {@link RestApi}
 */
public interface Api {
  Call<List<Session>> sessions();

  Call<Session> sessionForId(int id);

  Call<List<Speaker>> speakers();

  Call<List<Sponsor>> sponsors();

  /**
   * A POST request to submit feedback for a session
   *
   * @return an observable response to determine success or failure of call
   */
  Call<ResponseBody> submitFeedback(Session session, Feedback feedback);
}
