package org.selfconference.android.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.ui.drawer.DrawerItem;

public final class MainActivity extends BaseActivity {

  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;

  @Inject ViewContainer viewContainer;

  private ActionBarDrawerToggle drawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ViewGroup container = viewContainer.forActivity(this);

    getLayoutInflater().inflate(R.layout.activity_main, container);
    ButterKnife.bind(this, container);

    setSupportActionBar(getToolbar());

    drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));

    drawerToggle = new ActionBarDrawerToggle(this, //
        drawerLayout, //
        getToolbar(), //
        R.string.navigation_drawer_open, //
        R.string.navigation_drawer_close) {
      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        supportInvalidateOptionsMenu();
      }

      @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        supportInvalidateOptionsMenu();
      }
    };

    setupDrawerContent();
    clickNavigationDrawerItem(R.id.menu_item_sessions);
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  private void clickNavigationDrawerItem(@IdRes int menuIdRes) {
    MenuItem clickedMenuItem = navigationView.getMenu().findItem(menuIdRes);
    navigationItemSelectedListener.onNavigationItemSelected(clickedMenuItem);
  }

  private void setupDrawerContent() {
    navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
  }

  private void changeFragment(DrawerItem drawerItem) {
    getSupportFragmentManager() //
        .beginTransaction() //
        .replace(R.id.container, drawerItem.fragment(), drawerItem.fragmentTag()) //
        .commit();
  }

  private final OnNavigationItemSelectedListener navigationItemSelectedListener =
      new OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
          DrawerItem drawerItem = DrawerItem.fromMenuItem(menuItem);
          changeFragment(drawerItem);
          menuItem.setChecked(true);
          drawerLayout.closeDrawers();
          return true;
        }
      };
}
