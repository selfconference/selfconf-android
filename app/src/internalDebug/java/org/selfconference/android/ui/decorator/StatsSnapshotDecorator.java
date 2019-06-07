package org.selfconference.ui.decorator;

import com.squareup.picasso.StatsSnapshot;

public final class StatsSnapshotDecorator {

  private final StatsSnapshot snapshot;

  public static StatsSnapshotDecorator decorate(StatsSnapshot statsSnapshot) {
    return new StatsSnapshotDecorator(statsSnapshot);
  }

  private StatsSnapshotDecorator(StatsSnapshot snapshot) {
    this.snapshot = snapshot;
  }

  public String cacheSize() {
    String size = getSizeString(snapshot.size);
    String total = getSizeString(snapshot.maxSize);
    int percentage = (int) ((1f * snapshot.size / snapshot.maxSize) * 100);
    return size + " / " + total + " (" + percentage + "%)";
  }

  public String cacheHits() {
    return String.valueOf(snapshot.cacheHits);
  }

  public String cacheMisses() {
    return String.valueOf(snapshot.cacheMisses);
  }

  public String originalBitmapCount() {
    return String.valueOf(snapshot.originalBitmapCount);
  }

  public String totalOriginalBitmapSize() {
    return getSizeString(snapshot.totalOriginalBitmapSize);
  }

  public String averageOriginalBitmapSize() {
    return getSizeString(snapshot.averageOriginalBitmapSize);
  }

  public String transformedBitmapCount() {
    return String.valueOf(snapshot.transformedBitmapCount);
  }

  public String totalTransformedBitmapSize() {
    return getSizeString(snapshot.totalTransformedBitmapSize);
  }

  public String averageTransformedBitmapSize() {
    return getSizeString(snapshot.averageTransformedBitmapSize);
  }

  private static String getSizeString(long bytes) {
    String[] units = new String[] { "B", "KB", "MB", "GB" };
    int unit = 0;
    while (bytes >= 1024) {
      bytes /= 1024;
      unit += 1;
    }
    return bytes + units[unit];
  }
}
