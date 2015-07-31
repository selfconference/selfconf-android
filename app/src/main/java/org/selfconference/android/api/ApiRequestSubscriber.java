package org.selfconference.android.api;

import org.selfconference.android.R;
import rx.Subscriber;

import static org.selfconference.android.utils.Toaster.toast;

public class ApiRequestSubscriber<T> extends Subscriber<T> {
  @Override public void onCompleted() {

  }

  @Override public void onError(Throwable e) {
    toast(R.string.api_error);
  }

  @Override public void onNext(T t) {

  }
}
