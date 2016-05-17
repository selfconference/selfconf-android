package org.selfconference.android.data;

import com.google.common.collect.Lists;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.selfconference.android.data.api.model.Session;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static org.selfconference.android.data.Data.Status.LOADING;
import static org.selfconference.android.data.Data.Status.NONE;

@Singleton public final class DataSource {

  private final BehaviorSubject<Data<List<Session>>> sessionSubject;

  @Inject public DataSource() {
    Data<List<Session>> sessions = Data.<List<Session>>builder() //
        .data(Lists.newArrayList()) //
        .status(NONE) //
        .build();
    this.sessionSubject = BehaviorSubject.create(sessions);
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
    return this.sessionSubject.distinctUntilChanged().share();
  }
}
