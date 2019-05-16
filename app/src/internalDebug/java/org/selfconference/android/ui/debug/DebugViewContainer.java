package org.selfconference.android.ui.debug;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.mattprecious.telescope.EmailDeviceInfoLens;
import com.mattprecious.telescope.Lens;
import com.mattprecious.telescope.TelescopeLayout;

import org.selfconference.android.R;
import org.selfconference.android.ui.ViewContainer;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

@Singleton public final class DebugViewContainer implements ViewContainer {

  @Inject public DebugViewContainer() {
  }

  @Override public ViewGroup forActivity(Activity activity) {
    activity.setContentView(R.layout.debug_activity_frame);

    ViewHolder viewHolder = new ViewHolder();
    ButterKnife.bind(viewHolder, activity);

    ContextThemeWrapper contextThemeWrapper =
        new ContextThemeWrapper(activity, R.style.PurpleTheme);
    DebugView debugView = new DebugView(contextThemeWrapper);
    viewHolder.debugDrawer.addView(debugView);

    TelescopeLayout.cleanUp(activity);
    Lens lens = buildLens(activity);
    viewHolder.telescopeLayout.setLens(lens);

    return viewHolder.content;
  }

  private static Lens buildLens(Context context) {
    String applicationName = context.getString(R.string.application_name);
    String subject = context.getString(R.string.bug_report_email_subject, applicationName);
    String email = context.getString(R.string.bug_report_email_address);
    return new EmailDeviceInfoLens(context, subject, email);
  }

  static final class ViewHolder {
    @BindView(R.id.debug_drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.telescope_container) TelescopeLayout telescopeLayout;
    @BindView(R.id.debug_drawer_content) ViewGroup debugDrawer;
    @BindView(R.id.debug_content) FrameLayout content;
  }
}
