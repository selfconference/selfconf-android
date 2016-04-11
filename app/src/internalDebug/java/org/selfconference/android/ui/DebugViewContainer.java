package org.selfconference.android.ui;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.selfconference.android.R;

@Singleton public final class DebugViewContainer implements ViewContainer {

  @Inject public DebugViewContainer() {
  }

  @Override public ViewGroup forActivity(Activity activity) {
    activity.setContentView(R.layout.debug_activity_frame);

    ViewHolder viewHolder = new ViewHolder();
    ButterKnife.bind(viewHolder, activity);

    return viewHolder.content;
  }

  static final class ViewHolder {
    @Bind(R.id.debug_drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.debug_drawer) ViewGroup debugDrawer;
    @Bind(R.id.debug_drawer_content) FrameLayout content;
  }
}
