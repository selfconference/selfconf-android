package org.selfconference.android.schedule;


import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;

import butterknife.InjectView;

public class ScheduleFragment extends BaseFragment {

    public static final String TAG = ScheduleFragment.class.getName();

    @InjectView(R.id.pager_tab_strip)
    PagerSlidingTabStrip pagerTabStrip;

    @InjectView(R.id.schedule_view_pager)
    ViewPager scheduleViewPager;

    public ScheduleFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scheduleViewPager.setAdapter(new SessionFragmentPagerAdapter(getChildFragmentManager()));
        pagerTabStrip.setViewPager(scheduleViewPager);
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_schedule;
    }
}
