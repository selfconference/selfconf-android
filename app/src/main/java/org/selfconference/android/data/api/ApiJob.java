package org.selfconference.android.data.api;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.jobs.Priorities;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public abstract class ApiJob<T> extends Job {

  private static final long INITIAL_BACKOFF_MS = 1000;

  @Inject protected Api api;
  @Inject protected EventBus eventBus;

  protected ApiJob() {
    super(new Params(Priorities.DEFAULT).requireNetwork());
  }

  @Override protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
      int maxRunCount) {
    return RetryConstraint.createExponentialBackoff(runCount, INITIAL_BACKOFF_MS);
  }

  @Override protected void onCancel(int cancelReason) {
    Timber.i("Job %s canceled with reason %d", getId(), cancelReason);
  }

  @Override public void onRun() throws Throwable {
    Response<T> response = apiCall().execute();
    if (response.isSuccessful()) {
      onApiSuccess(response);
    } else {
      onApiFailure(response);
    }
  }

  @Override public void onAdded() {
    eventBus.post(onAddEvent());
  }

  protected abstract Object onAddEvent();

  protected abstract Call<T> apiCall();

  protected abstract void onApiSuccess(Response<T> response);

  protected abstract void onApiFailure(Response<T> response);
}
