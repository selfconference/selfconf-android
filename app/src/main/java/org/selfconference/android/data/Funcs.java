package org.selfconference.data;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public final class Funcs {

  public static <T> Function<T, T> identity() {
    return value -> value;
  }

  public static <T> Predicate<T> not(final Predicate<T> func) {
    return value -> !func.test(value);
  }

  private Funcs() {
    throw new AssertionError("No instances.");
  }
}
