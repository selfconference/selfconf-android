package org.selfconference.android.session;

import android.content.Context;
import android.support.annotation.DrawableRes;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SessionDetails {
    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static final class Builder {
        private final Context context;
        private final List<SessionDetail> sessionDetails = newArrayList();

        private Builder(Context context) {
            this.context = context;
        }

        public Builder add(@DrawableRes int iconResId, CharSequence info) {
            sessionDetails.add(
                    new SessionDetail(context.getResources().getDrawable(iconResId), info)
            );
            return this;
        }

        public List<SessionDetail> toList() {
            return sessionDetails;
        }
    }
}
