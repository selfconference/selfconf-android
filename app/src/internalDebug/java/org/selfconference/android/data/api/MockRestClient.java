package org.selfconference.data.api;

import com.squareup.moshi.Moshi;
import org.selfconference.data.api.model.Feedback;
import org.selfconference.data.api.model.Session;
import org.selfconference.data.api.model.Sponsor;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
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

  @Override public Observable<Result<List<Session>>> getSessions() {
    List<Session> sessions = MockSessions.allSessions();
    return delegate.returning(Calls.response(sessions)).getSessions();
  }

  @Override public Observable<Result<List<Sponsor>>> getSponsors() {
    List<Sponsor> sponsors = MockSponsors.allSponsors();
    return delegate.returning(Calls.response(sponsors)).getSponsors();
  }

  @Override public Observable<Result<ResponseBody>> submitFeedback(@Path("id") int id,
      @Body Feedback feedback) {
    String feedbackJson = moshi.adapter(Feedback.class).toJson(feedback);
    ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), feedbackJson);
    return delegate.returning(Calls.response(body)).submitFeedback(id, feedback);
  }
}
