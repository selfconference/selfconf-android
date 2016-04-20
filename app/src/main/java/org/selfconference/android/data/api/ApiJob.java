package org.selfconference.android.data.api;

import android.support.annotation.NonNull;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.data.job.Priorities;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A base {@link Job} for communicating with the Self.conference API.
 *
 * @param <T> the expected type returned by the API.
 */
public abstract class ApiJob<T> extends Job {

  private static final long INITIAL_BACKOFF_MS = 1000;

  @Inject protected RestClient restClient;
  @Inject protected EventBus eventBus;

  protected ApiJob() {
    super(new Params(Priorities.NORMAL).requireNetwork());
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
    Object addEvent = createAddEvent();
    checkNotNull(addEvent, "createAddEvent() must return a non-null object");

    eventBus.post(addEvent);
  }

  /** Returns an event to be posted when this job is added to the queue. Must not be null. */
  @NonNull protected abstract Object createAddEvent();

  /** Returns the API call to be executed when this job is run. */
  protected abstract Call<T> apiCall();

  /**
   * A callback to be invoked when the {@link #apiCall()} has succeeded.
   * Success is determined by a status code that falls within the range of [200, 300).
   *
   * @param response the response returned from {@link Call#execute()}.
   */
  protected abstract void onApiSuccess(Response<T> response);

  /**
   * A callback to be invoked when the {@link #apiCall()} has failed.
   * Failure is determined by a status code that does not fall within the range of [200, 300).
   *
   * @param response the response returned from {@link Call#execute()}.
   */
  protected abstract void onApiFailure(Response<T> response);
}
