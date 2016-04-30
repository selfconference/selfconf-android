package org.selfconference.android.ui.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.selfconference.android.util.Instants;
import org.threeten.bp.Instant;

public final class SessionFragmentPagerAdapter extends FragmentStatePagerAdapter {

  public SessionFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    return SessionListFragment.newInstance();
  }

  @Override public int getCount() {
    return Day.values().length;
  }

  @Override public CharSequence getPageTitle(int position) {
    Instant startTime = Day.fromPosition(position).getStartTime();
    return Instants.monthDayString(startTime);
  }
}
