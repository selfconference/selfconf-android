package org.selfconference.android.data;

import rx.Observable;
import rx.functions.Func1;

import static org.selfconference.android.data.Data.Status.ERROR;
import static org.selfconference.android.data.Data.Status.LOADED;
import static org.selfconference.android.data.Data.Status.LOADING;
import static org.selfconference.android.data.Data.Status.NONE;

public final class DataTransformers {

  public static <T> Observable.Transformer<Data<T>, Data<T>> none() {
    return dataObservable -> dataObservable.filter(byStatus(NONE));
  }

  public static <T> Observable.Transformer<Data<T>, Data<T>> loading() {
    return dataObservable -> dataObservable.filter(byStatus(LOADING));
  }

  public static <T> Observable.Transformer<Data<T>, T> loaded() {
    return dataObservable -> dataObservable.filter(byStatus(LOADED)).map(Data::data);
  }

  public static <T> Observable.Transformer<Data<T>, Throwable> error() {
    return dataObservable -> dataObservable.filter(byStatus(ERROR)).map(Data::throwable);
  }

  private static <T> Func1<Data<T>, Boolean> byStatus(Data.Status status) {
    return data -> data.status() == status;
  }

  private DataTransformers() {
    throw new AssertionError("No instances.");
  }
}
