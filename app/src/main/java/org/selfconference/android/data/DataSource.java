package org.selfconference.android.data;

import com.google.common.collect.Lists;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.pref.SessionPreferences;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static org.selfconference.android.data.Data.Status.LOADING;
import static org.selfconference.android.data.Data.Status.NONE;

@Singleton public final class DataSource {

  private final BehaviorSubject<Data<List<Session>>> sessionSubject;
  private final BehaviorSubject<Data<List<Sponsor>>> sponsorSubject;
  private final BehaviorSubject<Data<Event>> eventSubject;
  private final SessionPreferences sessionPreferences;

  @Inject public DataSource(SessionPreferences sessionPreferences) {
    Data<List<Session>> sessions = Data.<List<Session>>builder() //
        .data(Lists.newArrayList()) //
        .status(NONE) //
        .build();
    this.sessionSubject = BehaviorSubject.create(sessions);

    Data<List<Sponsor>> sponsors = Data.<List<Sponsor>>builder() //
        .data(Lists.newArrayList()) //
        .status(NONE) //
        .build();
    this.sponsorSubject = BehaviorSubject.create(sponsors);

    Data<Event> event = Data.<Event>builder() //
        .data(Event.empty()) //
        .status(NONE) //
        .build();
    this.eventSubject = BehaviorSubject.create(event);

    this.sessionPreferences = sessionPreferences;
  }

  public void setSessions(Data<List<Session>> data) {
    this.sessionSubject.onNext(data);
  }

  public void requestNewSessions() {
    Data<List<Session>> sessions = this.sessionSubject.getValue() //
        .toBuilder() //
        .status(LOADING) //
        .build();
    this.sessionSubject.onNext(sessions);
  }

  public Observable<Data<List<Session>>> sessions() {
    return this.sessionSubject.share().doOnSubscribe(this::tickleSessions);
  }

  public Observable<List<Session>> favoriteSessions() {
    return sessions().compose(DataTransformers.loaded())
        .flatMap(sessions -> Observable.from(sessions) //
            .filter(sessionPreferences::isFavorite) //
            .toList());
  }

  public void setSponsors(Data<List<Sponsor>> data) {
    this.sponsorSubject.onNext(data);
  }

  public void requestNewSponsors() {
    Data<List<Sponsor>> sponsors = this.sponsorSubject.getValue() //
        .toBuilder() //
        .status(LOADING) //
        .build();
    this.sponsorSubject.onNext(sponsors);
  }

  public Observable<Data<List<Sponsor>>> sponsors() {
    return this.sponsorSubject.share()
        .doOnSubscribe(() -> sponsorSubject.onNext(sponsorSubject.getValue()));
  }

  public void tickleSessions() {
    sessionSubject.onNext(sessionSubject.getValue());
  }

  public void setEvent(Data<Event> data) {
    this.eventSubject.onNext(data);
  }

  public void requestNewEvent() {
    Data<Event> event = this.eventSubject.getValue() //
        .toBuilder() //
        .status(LOADING) //
        .build();
    this.eventSubject.onNext(event);
  }

  public Observable<Data<Event>> event() {
    return this.eventSubject.share()
        .doOnSubscribe(() -> eventSubject.onNext(eventSubject.getValue()));
  }
}
