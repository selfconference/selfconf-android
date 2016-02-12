package org.selfconference.android.api;

import java.util.List;
import org.selfconference.android.feedback.FeedbackRequest;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * A Retrofit client to access the Self.conference API
 *
 * @see Api
 */
public interface SelfConferenceClient {
  @GET("/events/latest/sessions") //
  Observable<List<Session>> getSessions();

  @GET("/sessions/{id}") //
  Observable<Session> getSessionById(@Path("id") int id);

  @GET("/events/latest/speakers") //
  Observable<List<Speaker>> getSpeakers();

  @GET("/events/latest/sponsors") //
  Observable<List<Sponsor>> getSponsors();

  @POST("/sessions/{id}/feedbacks") //
  Observable<Response> submitFeedback(@Path("id") int id, @Body FeedbackRequest feedbackRequest);
}
