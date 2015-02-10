package org.selfconference.android.schedule;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScheduleFragment extends Fragment {

    public static final String TAG = ScheduleFragment.class.getName();

    @InjectView(R.id.pager_tab_strip)
    PagerSlidingTabStrip pagerTabStrip;

    @InjectView(R.id.schedule_view_pager)
    ViewPager scheduleViewPager;

    public ScheduleFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scheduleViewPager.setAdapter(new SessionFragmentPagerAdapter(getChildFragmentManager()));
        pagerTabStrip.setViewPager(scheduleViewPager);
    }
}
