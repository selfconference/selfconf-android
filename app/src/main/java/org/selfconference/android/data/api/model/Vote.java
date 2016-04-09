package org.selfconference.android.data.api.model;

public enum Vote {
  POSITIVE(1),
  NEGATIVE(-1);

  private final int asInt;

  Vote(int asInt) {
    this.asInt = asInt;
  }

  public int toInt() {
    return asInt;
  }
}
