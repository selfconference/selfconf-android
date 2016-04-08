package org.selfconference.android.api;

import java.util.List;
import okhttp3.ResponseBody;
import org.selfconference.android.feedback.FeedbackRequest;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * A Retrofit client to access the Self.conference API
 *
 * @see Api
 */
public interface SelfConferenceClient {
  @GET("events/latest/sessions") //
  Observable<List<Session>> getSessions();

  @GET("sessions/{id}") //
  Observable<Session> getSessionById(@Path("id") int id);

  @GET("events/latest/speakers") //
  Call<List<Speaker>> getSpeakers();

  @GET("events/latest/sponsors") //
  Call<List<Sponsor>> getSponsors();

  @POST("sessions/{id}/feedbacks") //
  Observable<Response<ResponseBody>> submitFeedback(@Path("id") int id,
      @Body FeedbackRequest feedbackRequest);
}
