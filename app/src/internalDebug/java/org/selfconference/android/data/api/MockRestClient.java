package org.selfconference.android.data.api;

import com.squareup.moshi.Moshi;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;

@Singleton public final class MockRestClient implements RestClient {

  private final BehaviorDelegate<RestClient> delegate;
  private final Moshi moshi;

  @Inject public MockRestClient(MockRetrofit mockRetrofit, Moshi moshi) {
    this.delegate = mockRetrofit.create(RestClient.class);
    this.moshi = moshi;
  }

  @Override public Call<List<Session>> getSessions() {
    List<Session> sessions = MockSessions.allSessions();
    return delegate.returning(Calls.response(sessions)).getSessions();
  }

  @Override public Call<Session> getSessionById(@Path("id") int id) {
    Session session = MockSessions.findSessionById(id);
    return delegate.returning(Calls.response(session)).getSessionById(id);
  }

  @Override public Call<List<Speaker>> getSpeakers() {
    List<Speaker> speakers = MockSpeakers.allSpeakers();
    return delegate.returning(Calls.response(speakers)).getSpeakers();
  }

  @Override public Call<List<Sponsor>> getSponsors() {
    List<Sponsor> sponsors = MockSponsors.allSponsors();
    return delegate.returning(Calls.response(sponsors)).getSponsors();
  }

  @Override public Call<ResponseBody> submitFeedback(@Path("id") int id, @Body Feedback feedback) {
    String feedbackJson = moshi.adapter(Feedback.class).toJson(feedback);
    ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), feedbackJson);
    return delegate.returning(Calls.response(body)).submitFeedback(id, feedback);
  }
}
