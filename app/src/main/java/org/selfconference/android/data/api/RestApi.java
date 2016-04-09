package org.selfconference.android.data.api;

import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.selfconference.android.App;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;

public final class RestApi implements Api {

  @Inject RestClient client;

  public RestApi() {
    App.getInstance().inject(this);
  }

  @Override public Call<List<Session>> getSessions() {
    return client.getSessions();
  }

  @Override public Call<List<Speaker>> getSpeakers() {
    return client.getSpeakers();
  }

  @Override public Call<List<Sponsor>> getSponsors() {
    return client.getSponsors();
  }

  @Override public Call<ResponseBody> submitFeedback(Session session, Feedback feedback) {
    return client.submitFeedback(session.id(), feedback);
  }

  @Override public Call<Session> getSessionById(int id) {
    return client.getSessionById(id);
  }
}
