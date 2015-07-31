package org.selfconference.android.utils.rx;

import android.support.v4.widget.SwipeRefreshLayout;
import rx.Observable;
import rx.Observable.Transformer;
import rx.functions.Action0;

public final class Transformers {

  public static <T> Transformer<T, T> setRefreshing(final SwipeRefreshLayout swipeRefreshLayout) {
    return new Transformer<T, T>() {
      @Override public Observable<T> call(Observable<T> observable) {
        return observable //
            .doOnSubscribe(new Action0() {
              @Override public void call() {
                swipeRefreshLayout.post(new Runnable() {
                  @Override public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                  }
                });
              }
            }) //
            .doOnTerminate(new Action0() {
              @Override public void call() {
                swipeRefreshLayout.post(new Runnable() {
                  @Override public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                  }
                });
              }
            });
      }
    };
  }

  private Transformers() {
    throw new AssertionError("No instances.");
  }
}
