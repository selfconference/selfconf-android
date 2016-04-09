package org.selfconference.android.ui.drawer;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import org.selfconference.android.R;
import org.selfconference.android.ui.coc.CodeOfConductFragment;
import org.selfconference.android.ui.session.SessionContainerFragment;
import org.selfconference.android.ui.speaker.SpeakerListFragment;
import org.selfconference.android.ui.sponsor.SponsorListFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public enum DrawerItem {
  SESSIONS {
    @Override public Fragment fragment() {
      return new SessionContainerFragment();
    }

    @Override public String fragmentTag() {
      return SessionContainerFragment.TAG;
    }
  },
  SPEAKERS {
    @Override public Fragment fragment() {
      return new SpeakerListFragment();
    }

    @Override public String fragmentTag() {
      return SpeakerListFragment.TAG;
    }
  },
  SPONSORS {
    @Override public Fragment fragment() {
      return new SponsorListFragment();
    }

    @Override public String fragmentTag() {
      return SponsorListFragment.TAG;
    }
  },
  CODE_OF_CONDUCT {
    @Override public Fragment fragment() {
      return new CodeOfConductFragment();
    }

    @Override public String fragmentTag() {
      return CodeOfConductFragment.TAG;
    }
  };

  public abstract Fragment fragment();

  public abstract String fragmentTag();

  public static DrawerItem fromMenuItem(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    switch (menuItem.getItemId()) {
      case R.id.menu_item_sessions:
        return SESSIONS;
      case R.id.menu_item_speakers:
        return SPEAKERS;
      case R.id.menu_item_sponsors:
        return SPONSORS;
      case R.id.menu_item_code_of_conduct:
        return CODE_OF_CONDUCT;
      default:
        throw new IllegalStateException("menuItemId was not passed in from @menu/drawer_view");
    }
  }
}
