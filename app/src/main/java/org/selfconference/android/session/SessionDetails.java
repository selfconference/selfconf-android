package org.selfconference.android.session;

import android.support.annotation.DrawableRes;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.selfconference.android.utils.ResourceProvider.getDrawable;

public final class SessionDetails {
  public static Builder builder() {
    return new Builder();
  }

  private SessionDetails() {
    throw new AssertionError("No instances.");
  }

  public static final class Builder {
    private final List<SessionDetail> sessionDetails = newArrayList();

    public Builder add(@DrawableRes int iconResId, CharSequence info) {
      sessionDetails.add(SessionDetail.create(getDrawable(iconResId), info));
      return this;
    }

    public List<SessionDetail> toList() {
      return sessionDetails;
    }
  }
}
