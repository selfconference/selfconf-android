package org.selfconference.data;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;

import static org.selfconference.data.Data.Status.ERROR;
import static org.selfconference.data.Data.Status.LOADED;
import static org.selfconference.data.Data.Status.LOADING;
import static org.selfconference.data.Data.Status.NONE;

public final class DataTransformers {

  public static <T> ObservableTransformer<Data<T>, Data<T>> none() {
    return dataObservable -> dataObservable.filter(byStatus(NONE));
  }

  public static <T> ObservableTransformer<Data<T>, Data<T>> loading() {
    return dataObservable -> dataObservable.filter(byStatus(LOADING));
  }

  public static <T> ObservableTransformer<Data<T>, T> loaded() {
    return dataObservable -> dataObservable.filter(byStatus(LOADED)).map(Data::data);
  }

  public static <T> ObservableTransformer<Data<T>, Throwable> error() {
    return dataObservable -> dataObservable.filter(byStatus(ERROR)).map(Data::throwable);
  }

  private static <T> Predicate<Data<T>> byStatus(Data.Status status) {
    return data -> data.status() == status;
  }

  private DataTransformers() {
    throw new AssertionError("No instances.");
  }
}
