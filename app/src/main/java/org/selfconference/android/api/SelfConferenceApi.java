package org.selfconference.android.api;

import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.selfconference.android.App;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.feedback.Feedback;
import retrofit2.Call;
import rx.functions.Func2;

public final class SelfConferenceApi implements Api {

  @Inject SelfConferenceClient client;

  public SelfConferenceApi() {
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

  private static Func2<Session, Session, Integer> sortByDate() {
    return (session, session2) -> session.beginning().compareTo(session2.beginning());
  }
}
