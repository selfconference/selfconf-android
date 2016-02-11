package org.selfconference.android.api;

import com.google.common.base.Optional;

import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.selfconference.android.App;
import org.selfconference.android.feedback.Feedback;
import org.selfconference.android.feedback.FeedbackRequest;
import org.selfconference.android.session.Day;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;
import retrofit.client.Response;
import rx.Observable;
import rx.functions.Func2;

import static com.google.common.base.Optional.fromNullable;
import static org.joda.time.DateTime.now;
import static org.selfconference.android.utils.DateTimeHelper.intervalForDay;

public final class SelfConferenceApi implements Api {

  @Inject SelfConferenceClient client;

  public SelfConferenceApi() {
    App.getInstance().inject(this);
  }

  @Override public Observable<List<Session>> getSessions() {
    return client.getSessions();
  }

  @Override public Observable<List<Speaker>> getSpeakers() {
    return client.getSpeakers();
  }

  @Override public Observable<List<Sponsor>> getSponsors() {
    return client.getSponsors();
  }

  @Override public Observable<Response> submitFeedback(Session session, Feedback feedback) {
    return client.submitFeedback(session.getId(), new FeedbackRequest(feedback));
  }

  @Override public Observable<List<Session>> getSessionsByDay(Day day) {
    return getSessions() //
            .flatMap(Observable::from) //
            .filter(session -> intervalForDay(day).contains(session.getBeginning()) || session.getBeginning() == null) //
            .toSortedList(sortByDate());
  }

  @Override public Observable<Session> getSessionById(int id) {
    return client.getSessionById(id);
  }

  private static Func2<Session, Session, Integer> sortByDate() {
    return (session, session2) -> {
      DateTime now = now();
      return fromNullable(session.getBeginning()).or(now).compareTo(fromNullable(session2.getBeginning()).or(now));
    };
  }
}
