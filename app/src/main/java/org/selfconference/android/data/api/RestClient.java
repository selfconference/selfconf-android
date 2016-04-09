package org.selfconference.android.data.api;

import java.util.List;
import okhttp3.ResponseBody;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.Feedback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * A Retrofit client to access the Self.conference API
 *
 * @see Api
 */
public interface RestClient {
  @GET("events/latest/sessions") //
  Call<List<Session>> getSessions();

  @GET("sessions/{id}") //
  Call<Session> getSessionById(@Path("id") int id);

  @GET("events/latest/speakers") //
  Call<List<Speaker>> getSpeakers();

  @GET("events/latest/sponsors") //
  Call<List<Sponsor>> getSponsors();

  @POST("sessions/{id}/feedbacks") //
  Call<ResponseBody> submitFeedback(@Path("id") int id, @Body Feedback feedback);
}
