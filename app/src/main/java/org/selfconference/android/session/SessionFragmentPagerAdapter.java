package org.selfconference.android.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.api.Day;

public class SessionFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public SessionFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        final Day day = Day.fromPosition(position);
        return SessionListFragment.newInstance(day);
    }

    @Override public int getCount() {
        return Day.values().length;
    }

    @Override public CharSequence getPageTitle(int position) {
        return App.getInstance().getString(position == 0 ? R.string.friday : R.string.saturday);
    }
}
