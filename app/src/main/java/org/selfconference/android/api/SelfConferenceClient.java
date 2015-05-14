package org.selfconference.android.api;

import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface SelfConferenceClient {
    @GET("/sessions") Observable<List<Session>> getSessions();

    @GET("/sessions/{id}") Observable<Session> getSessionById(@Path("id") final int id);

    @GET("/speakers") Observable<List<Speaker>> getSpeakers();
}
