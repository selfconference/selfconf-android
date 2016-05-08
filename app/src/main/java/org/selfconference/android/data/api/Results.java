package org.selfconference.android.data.api;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

public final class Results {
  private static final Func1<Result<?>, Boolean> SUCCESSFUL =
      result -> !result.isError() && result.response().isSuccessful();

  public static Func1<Result<?>, Boolean> isSuccessful() {
    return SUCCESSFUL;
  }

  public static <T> Func1<Result<? extends T>, T> responseBody() {
    return result -> result.response().body();
  }

  private Results() {
    throw new AssertionError("No instances.");
  }
}
