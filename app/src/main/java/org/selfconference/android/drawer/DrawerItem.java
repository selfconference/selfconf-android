package org.selfconference.android.drawer;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import org.selfconference.android.R;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.speakers.SpeakerListFragment;
import org.selfconference.android.sponsors.SponsorListFragment;

public enum DrawerItem {
  SESSIONS {
    @Override public Fragment getFragment() {
      return new SessionContainerFragment();
    }

    @Override public String getFragmentTag() {
      return SessionContainerFragment.TAG;
    }
  },
  SPEAKERS {
    @Override public Fragment getFragment() {
      return new SpeakerListFragment();
    }

    @Override public String getFragmentTag() {
      return SpeakerListFragment.TAG;
    }
  },
  SPONSORS {
    @Override public Fragment getFragment() {
      return new SponsorListFragment();
    }

    @Override public String getFragmentTag() {
      return SponsorListFragment.TAG;
    }
  },
  CODE_OF_CONDUCT {
    @Override public Fragment getFragment() {
      return new CodeOfConductFragment();
    }

    @Override public String getFragmentTag() {
      return CodeOfConductFragment.TAG;
    }
  },
  SETTINGS {
    @Override public Fragment getFragment() {
      return null;
    }

    @Override public String getFragmentTag() {
      return null;
    }
  };

  public abstract Fragment getFragment();

  public abstract String getFragmentTag();

  public static DrawerItem fromMenuItem(@NonNull MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.menu_item_sessions:
        return SESSIONS;
      case R.id.menu_item_speakers:
        return SPEAKERS;
      case R.id.menu_item_sponsors:
        return SPONSORS;
      case R.id.menu_item_code_of_conduct:
        return CODE_OF_CONDUCT;
      case R.id.menu_item_settings:
        return SETTINGS;
    }
    throw new IllegalStateException("menuItemId was not passed in from @menu/drawer_view");
  }
}
