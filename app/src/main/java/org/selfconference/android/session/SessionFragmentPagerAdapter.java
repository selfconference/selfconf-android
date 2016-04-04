package org.selfconference.android.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.joda.time.DateTime;
import org.selfconference.android.ui.decorators.DateTimeDecorator;

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
    DateTime startTime = Day.fromPosition(position).getStartTime();
    DateTimeDecorator decorator = DateTimeDecorator.fromDateTime(startTime);
    return decorator.shortDateString();
  }
}
