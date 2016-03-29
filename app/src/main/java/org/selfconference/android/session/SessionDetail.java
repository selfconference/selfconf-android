package org.selfconference.android.session;

import android.graphics.drawable.Drawable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class SessionDetail {

  public static SessionDetail create(Drawable drawable, CharSequence info) {
    return new AutoValue_SessionDetail(drawable, info);
  }

  SessionDetail() {}

  public abstract Drawable drawable();

  public abstract CharSequence info();
}
