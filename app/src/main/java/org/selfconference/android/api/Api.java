package org.selfconference.android.api;

import org.selfconference.android.feedback.Feedback;
import org.selfconference.android.session.Day;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;

import java.util.List;

import retrofit.client.Response;
import rx.Observable;

/**
 * A wrapper around the Self.conference API
 * <p/>
 * Current implementations include {@link SelfConferenceApi}
 */
public interface Api {
    Observable<List<Session>> getSessions();

    Observable<List<Session>> getSessionsByDay(final Day day);

    Observable<Session> getSessionById(final int id);

    Observable<List<Speaker>> getSpeakers();

    Observable<List<Sponsor>> getSponsors();

    /**
     * A POST request to submit feedback for a session
     *
     * @param session
     * @param feedback
     * @return an observable response to determine success or failure of call
     */
    Observable<Response> submitFeedback(Session session, Feedback feedback);
}
