package org.selfconference.android.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public final class SessionFragmentPagerAdapter extends FragmentStatePagerAdapter {

  public SessionFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    Day day = Day.fromPosition(position);
    return SessionListFragment.newInstance(day);
  }

  @Override public int getCount() {
    return Day.values().length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return Day.fromPosition(position).getStartTime().toString("MMM d");
  }
}
