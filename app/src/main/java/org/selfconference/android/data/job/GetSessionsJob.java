package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import com.google.common.base.Optional;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.event.GetSessionsAddEvent;
import org.selfconference.android.data.event.GetSessionsSuccessEvent;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public final class GetSessionsJob extends ApiJob<List<Session>> {
  @NonNull @Override protected Object createAddEvent() {
    return new GetSessionsAddEvent();
  }

  @Override protected Observable<Result<List<Session>>> apiCall() {
    return restClient.getSessions();
  }

  @Override protected void onApiSuccess(Response<List<Session>> response) {
    List<Session> sortedSessions = Ordering.from(new Comparator<Session>() {
      @Override public int compare(Session lhs, Session rhs) {
        Slot left = Optional.fromNullable(lhs.slot()).or(Slot.empty());
        Slot right = Optional.fromNullable(rhs.slot()).or(Slot.empty());
        return ComparisonChain.start() //
            .compare(left, right) //
            .result();
      }
    }).sortedCopy(response.body());
    eventBus.post(new GetSessionsSuccessEvent(ImmutableList.copyOf(sortedSessions)));
  }

  @Override protected void onApiFailure(Throwable error) {

  }
}
