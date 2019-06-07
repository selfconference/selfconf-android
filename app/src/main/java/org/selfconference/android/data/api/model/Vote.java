package org.selfconference.data.api.model;

public enum Vote {
  POSITIVE(1),
  NEGATIVE(-1);

  private final int asInt;

  public static Vote parseInt(int asInt) {
    switch (asInt) {
      case 1: return POSITIVE;
      case -1: return NEGATIVE;
      default: throw new IllegalArgumentException("Invalid Vote int: " + asInt);
    }
  }

  Vote(int asInt) {
    this.asInt = asInt;
  }

  public int toInt() {
    return asInt;
  }
}
