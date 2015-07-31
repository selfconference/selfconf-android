package org.selfconference.android.session;

import android.graphics.drawable.Drawable;

public final class SessionDetail {
  public final Drawable drawable;
  public final CharSequence info;

  public SessionDetail(Drawable drawable, CharSequence info) {
    this.drawable = drawable;
    this.info = info;
  }
}
