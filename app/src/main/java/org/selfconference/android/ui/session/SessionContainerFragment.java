package org.selfconference.android.ui.session;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import butterknife.Bind;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.R;

public final class SessionContainerFragment extends BaseFragment {
  public static final String TAG = SessionContainerFragment.class.getName();

  @Bind(R.id.tabs) TabLayout tabLayout;
  @Bind(R.id.schedule_view_pager) ViewPager scheduleViewPager;

  public SessionContainerFragment() {
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    scheduleViewPager.setAdapter(new SessionFragmentPagerAdapter(getChildFragmentManager()));
    tabLayout.setupWithViewPager(scheduleViewPager);
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule;
  }
}
