package org.selfconference.android.data.jobs;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.App;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.api.SponsorComparator;
import org.selfconference.android.data.events.GetSponsorsAddEvent;
import org.selfconference.android.data.events.GetSponsorsSuccessEvent;
import org.selfconference.android.sponsors.Sponsor;
import retrofit2.Response;
import timber.log.Timber;

public final class GetSponsorsJob extends Job {

  @Inject Api api;
  @Inject EventBus eventBus;

  public static Job create() {
    return new GetSponsorsJob();
  }

  private GetSponsorsJob() {
    super(new Params(Priorities.DEFAULT).requireNetwork());
    App.getInstance().inject(this);
  }

  @Override public void onAdded() {
    eventBus.post(new GetSponsorsAddEvent());
  }

  @Override public void onRun() throws Throwable {
    Response<List<Sponsor>> response = api.getSponsors().execute();
    if (response.isSuccessful()) {
      ImmutableList<Sponsor> sponsors = Ordering.from(new SponsorComparator()) //
          .immutableSortedCopy(response.body());
      eventBus.post(new GetSponsorsSuccessEvent(sponsors));
    } else {
      // TODO handle error
      Timber.e("Failure retrieving sponsors: %s", response.errorBody().string());
    }
  }

  @Override protected void onCancel(int cancelReason) {

  }

  @Override protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
      int maxRunCount) {
    return RetryConstraint.createExponentialBackoff(runCount, 1000);
  }
}
