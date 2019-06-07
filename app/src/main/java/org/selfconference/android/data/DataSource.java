package org.selfconference.data;

import com.google.common.collect.Lists;
import org.selfconference.data.api.model.Session;
import org.selfconference.data.api.model.Sponsor;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

import static org.selfconference.data.Data.Status.LOADING;
import static org.selfconference.data.Data.Status.NONE;

@Singleton public final class DataSource {

  private final BehaviorSubject<Data<List<Session>>> sessionSubject;
  private final BehaviorSubject<Data<List<Sponsor>>> sponsorSubject;

  @Inject public DataSource() {
    Data<List<Session>> sessions = Data.<List<Session>>builder()
        .data(Lists.newArrayList())
        .status(NONE)
        .build();
    this.sessionSubject = BehaviorSubject.createDefault(sessions);

    Data<List<Sponsor>> sponsors = Data.<List<Sponsor>>builder()
        .data(Lists.newArrayList())
        .status(NONE)
        .build();
    this.sponsorSubject = BehaviorSubject.createDefault(sponsors);
  }

  public void setSessions(Data<List<Session>> data) {
    this.sessionSubject.onNext(data);
  }

  public void requestNewSessions() {
    Data<List<Session>> sessions = this.sessionSubject.getValue()
        .toBuilder()
        .status(LOADING)
        .build();
    this.sessionSubject.onNext(sessions);
  }

  public Observable<Data<List<Session>>> sessions() {
    return this.sessionSubject.share()
        .doOnSubscribe(d -> tickleSessions())
        .doOnError(this::logError);
  }

  public void setSponsors(Data<List<Sponsor>> data) {
    this.sponsorSubject.onNext(data);
  }

  public void requestNewSponsors() {
    Data<List<Sponsor>> sponsors = this.sponsorSubject.getValue()
        .toBuilder()
        .status(LOADING)
        .build();
    this.sponsorSubject.onNext(sponsors);
  }

  public Observable<Data<List<Sponsor>>> sponsors() {
    return this.sponsorSubject.share()
        .doOnSubscribe(d -> tickleSponsors())
        .doOnError(this::logError);
  }

  public void tickleSessions() {
    sessionSubject.onNext(sessionSubject.getValue());
  }

  public void tickleSponsors() {
    sponsorSubject.onNext(sponsorSubject.getValue());
  }

  private void logError(Throwable throwable) {
    Timber.e(throwable, "Error occurred in observable data stream");
  }
}
