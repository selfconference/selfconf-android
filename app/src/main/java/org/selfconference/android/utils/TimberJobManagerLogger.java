package org.selfconference.android.utils;

import com.birbit.android.jobqueue.log.CustomLogger;
import org.selfconference.android.BuildConfig;
import timber.log.Timber;

public final class TimberJobManagerLogger implements CustomLogger {
  @Override public boolean isDebugEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override public void d(String text, Object... args) {
    Timber.d(text, args);
  }

  @Override public void e(Throwable t, String text, Object... args) {
    Timber.e(t, text, args);
  }

  @Override public void e(String text, Object... args) {
    Timber.e(text, args);
  }
}
