package org.selfconference.android.data.job;

import android.support.annotation.NonNull;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.List;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.event.GetSessionsAddEvent;
import org.selfconference.android.data.event.GetSessionsSuccessEvent;
import retrofit2.Call;
import retrofit2.Response;

public final class GetSessionsJob extends ApiJob<List<Session>> {
  @NonNull @Override protected Object createAddEvent() {
    return new GetSessionsAddEvent();
  }

  @Override protected Call<List<Session>> apiCall() {
    return restClient.getSessions();
  }

  @Override protected void onApiSuccess(Response<List<Session>> response) {
    List<Session> sortedSessions = Ordering.from(new Comparator<Session>() {
      @Override public int compare(Session lhs, Session rhs) {
        return ComparisonChain.start() //
            .compare(lhs.slot(), rhs.slot()) //
            .result();
      }
    }).sortedCopy(response.body());
    eventBus.post(new GetSessionsSuccessEvent(ImmutableList.copyOf(sortedSessions)));
  }

  @Override protected void onApiFailure(Response<List<Session>> response) {

  }
}
