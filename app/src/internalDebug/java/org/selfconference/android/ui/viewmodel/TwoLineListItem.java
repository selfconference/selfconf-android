package org.selfconference.ui.viewmodel;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class TwoLineListItem {
  public static TwoLineListItem create(@NonNull String lineOne, @NonNull String lineTwo) {
    return new AutoValue_TwoLineListItem(lineOne, lineTwo);
  }

  @NonNull public abstract String lineOne();

  @NonNull public abstract String lineTwo();
}
