package org.selfconference.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import org.selfconference.android.drawer.DrawerItem;
import org.selfconference.android.settings.SettingsActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static org.selfconference.android.drawer.DrawerItem.SETTINGS;
import static org.selfconference.android.utils.ResourceProvider.getColor;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.navigation_view) NavigationView navigationView;

    private ActionBarDrawerToggle drawerToggle;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(getToolbar());

        drawerLayout.setStatusBarBackgroundColor(getColor(R.color.green_dark));

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                getToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
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
        navigationItemSelectedListener.onNavigationItemSelected(navigationView.getMenu().findItem(menuIdRes));
    }

    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private void changeFragment(DrawerItem drawerItem) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, drawerItem.getFragment(), drawerItem.getFragmentTag())
                .commit();
    }

    private final OnNavigationItemSelectedListener navigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            final DrawerItem drawerItem = DrawerItem.fromMenuItem(menuItem);
            if (drawerItem != SETTINGS) {
                changeFragment(drawerItem);
                menuItem.setChecked(true);
            } else {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
            drawerLayout.closeDrawers();
            return true;
        }
    };
}
