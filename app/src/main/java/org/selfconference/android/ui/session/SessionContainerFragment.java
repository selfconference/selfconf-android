package org.selfconference.android.ui.session;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import org.selfconference.android.R;
import org.selfconference.android.ui.BaseFragment;

public final class SessionContainerFragment extends BaseFragment {
  public static final String TAG = SessionContainerFragment.class.getName();

  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.schedule_view_pager) ViewPager scheduleViewPager;

  public SessionContainerFragment() {
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    scheduleViewPager.setAdapter(new SessionFragmentPagerAdapter(getChildFragmentManager()));
    scheduleViewPager.setOffscreenPageLimit(2);
    tabLayout.setupWithViewPager(scheduleViewPager);
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule;
  }
}
