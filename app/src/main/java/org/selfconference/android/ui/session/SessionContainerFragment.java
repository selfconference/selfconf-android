package org.selfconference.android.ui.session;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import org.selfconference.android.R;
import org.selfconference.android.ui.BaseFragment;
import butterknife.BindView;

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
