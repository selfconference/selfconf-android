package org.selfconference.android.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import org.selfconference.android.R;

/**
 * A {@link SwipeRefreshLayout} pre-populated with Self.conference's brand colors.
 */
public final class SelfConferenceSwipeRefreshLayout extends SwipeRefreshLayout {
    public SelfConferenceSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SelfConferenceSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(
                R.color.primary,
                R.color.accent,
                R.color.red,
                R.color.purple
        );
    }
}
