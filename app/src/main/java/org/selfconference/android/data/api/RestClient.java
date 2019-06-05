package org.selfconference.android.data.api;

import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Sponsor;
import java.util.List;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * A Retrofit client to access the Self.conference API.
 */
public interface RestClient {
  @GET("events/latest/sessions")
  Observable<Result<List<Session>>> getSessions();

  @GET("events/latest/sponsors")
  Observable<Result<List<Sponsor>>> getSponsors();

  @POST("sessions/{id}/feedbacks")
  Observable<Result<ResponseBody>> submitFeedback(@Path("id") int id, @Body Feedback feedback);
}
