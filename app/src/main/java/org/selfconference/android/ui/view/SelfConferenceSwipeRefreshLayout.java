package org.selfconference.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import org.selfconference.R;

/**
 * A {@link SwipeRefreshLayout} pre-populated with Self.conference's brand colors.
 */
public final class SelfConferenceSwipeRefreshLayout extends SwipeRefreshLayout {
  public SelfConferenceSwipeRefreshLayout(Context context) {
    this(context, null);
  }

  public SelfConferenceSwipeRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    setColorSchemeResources(R.color.green, R.color.orange, R.color.red, R.color.purple);
  }
}
