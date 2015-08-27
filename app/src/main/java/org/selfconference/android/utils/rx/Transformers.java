package org.selfconference.android.utils.rx;

import android.support.v4.widget.SwipeRefreshLayout;
import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class Transformers {

  public static <T> Transformer<T, T> setRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
    return observable -> observable //
        .doOnSubscribe(
            () -> swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true))) //
        .doOnTerminate(
            () -> swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false)));
  }

  /**
   * Composes an {@link Observable} to subscribe an I/O thread scheduler
   * and observe the result on the UI thread.
   */
  public static <T> Transformer<T, T> ioSchedulers() {
    return observable -> observable //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread());
  }

  private Transformers() {
    throw new AssertionError("No instances.");
  }
}
