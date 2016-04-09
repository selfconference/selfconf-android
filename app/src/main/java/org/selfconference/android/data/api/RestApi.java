package org.selfconference.android.data.api;

import java.util.List;
import okhttp3.ResponseBody;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;

public final class RestApi implements Api {

  private final RestClient client;

  public RestApi(RestClient client) {
    this.client = client;
  }

  @Override public Call<List<Session>> sessions() {
    return client.getSessions();
  }

  @Override public Call<List<Speaker>> speakers() {
    return client.getSpeakers();
  }

  @Override public Call<List<Sponsor>> sponsors() {
    return client.getSponsors();
  }

  @Override public Call<ResponseBody> submitFeedback(Session session, Feedback feedback) {
    return client.submitFeedback(session.id(), feedback);
  }

  @Override public Call<Session> sessionForId(int id) {
    return client.getSessionById(id);
  }
}
