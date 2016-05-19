package org.selfconference.android.util;

import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import timber.log.Timber;

public final class CrashlyticsTree extends Timber.Tree {
  @Override protected void log(int priority, String tag, String message, Throwable t) {
    CrashlyticsCore crashlyticsCore = Crashlytics.getInstance().core;

    crashlyticsCore.log(priority, tag, message);

    if (t == null) {
      return;
    }
    if (priority == Log.ERROR) {
      crashlyticsCore.logException(t);
    } else {
      crashlyticsCore.log(priority, tag, Log.getStackTraceString(t));
    }
  }

  @Override protected boolean isLoggable(int priority) {
    return !(priority == Log.DEBUG || priority == Log.VERBOSE);
  }
}
