package org.selfconference.android.schedule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.selfconference.android.App;
import org.selfconference.android.R;

public class SessionFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_DAYS = 2;

    public SessionFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new DaySessionFragment();
    }

    @Override
    public int getCount() {
        return NUM_DAYS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return App.getInstance().getString(R.string.day_number, position + 1);
    }
}
