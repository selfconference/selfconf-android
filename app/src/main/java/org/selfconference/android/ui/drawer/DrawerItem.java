package org.selfconference.ui.drawer;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.selfconference.R;
import org.selfconference.ui.coc.CodeOfConductFragment;
import org.selfconference.ui.session.SessionContainerFragment;
import org.selfconference.ui.sponsor.SponsorListFragment;

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
      case R.id.menu_item_sponsors:
        return SPONSORS;
      case R.id.menu_item_code_of_conduct:
        return CODE_OF_CONDUCT;
      default:
        throw new IllegalStateException("menuItemId was not passed in from @menu/drawer_view");
    }
  }
}
