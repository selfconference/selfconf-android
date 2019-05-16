package org.selfconference.android.ui.viewmodel;

import androidx.annotation.DrawableRes;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class SessionDetail {

  public static SessionDetail create(int drawableResId, CharSequence info) {
    return new AutoValue_SessionDetail(drawableResId, info);
  }

  SessionDetail() {}

  @DrawableRes public abstract int drawableResId();

  public abstract CharSequence info();
}
