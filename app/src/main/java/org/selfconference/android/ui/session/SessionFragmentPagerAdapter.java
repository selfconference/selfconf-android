package org.selfconference.ui.session;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import org.selfconference.util.Instants;
import org.threeten.bp.Instant;

public final class SessionFragmentPagerAdapter extends FragmentStatePagerAdapter {

  public SessionFragmentPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    if (position == 0 || position == 1) {
      return SessionListFragment.newInstance(Day.fromPosition(position));
    }
    return MyScheduleFragment.newInstance();
  }

  @Override public int getCount() {
    return Day.values().length + 1;
  }

  @Override public CharSequence getPageTitle(int position) {
    if (position == 0 || position == 1) {
      Instant startTime = Day.fromPosition(position).getStartTime();
      return Instants.monthDayString(startTime);
    }
    return "My Schedule";
  }
}
