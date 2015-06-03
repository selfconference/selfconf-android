package org.selfconference.android.session;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;

import butterknife.InjectView;

public class SessionContainerFragment extends BaseFragment {
    public static final String TAG = SessionContainerFragment.class.getName();

    @InjectView(R.id.tabs) TabLayout tabLayout;
    @InjectView(R.id.schedule_view_pager) ViewPager scheduleViewPager;

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
