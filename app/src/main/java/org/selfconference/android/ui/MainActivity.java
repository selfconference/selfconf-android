package org.selfconference.android.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.common.collect.Lists;
import de.psdev.licensesdialog.LicensesDialog;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.BuildConfig;
import org.selfconference.android.R;
import org.selfconference.android.data.Data;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.Funcs;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.ui.drawer.DrawerItem;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import static org.selfconference.android.data.Data.Status.ERROR;
import static org.selfconference.android.data.Data.Status.LOADED;

public final class MainActivity extends BaseActivity implements FragmentCallbacks {

  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;

  @Inject ViewContainer viewContainer;
  @Inject DataSource dataSource;
  @Inject RestClient restClient;

  private final PublishSubject<Void> sessionsSubject = PublishSubject.create();
  private final PublishSubject<Void> sponsorsSubject = PublishSubject.create();

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

    Observable<Result<List<Session>>> sessionsResult =
        sessionsSubject.flatMap(__ -> restClient.getSessions().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    sessionsResult.filter(Results.isSuccessful()) //
        .map(Results.responseBody()) //
        .compose(bindToLifecycle()) //
        .subscribe(sessions -> {
          dataSource.setSessions(
              Data.<List<Session>>builder().data(sessions).status(LOADED).build());
        });

    sessionsResult.filter(Funcs.not(Results.isSuccessful())) //
        .compose(bindToLifecycle()) //
        .subscribe(sessionResult -> {
          dataSource.setSessions(Data.<List<Session>>builder().data(Lists.newArrayList())
              .status(ERROR)
              .throwable(sessionResult.error())
              .build());
        });

    onRequestSessions();

    Observable<Result<List<Sponsor>>> sponsorsResult =
        sessionsSubject.flatMap(__ -> restClient.getSponsors().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    sponsorsResult.filter(Results.isSuccessful()) //
        .map(Results.responseBody()) //
        .compose(bindToLifecycle()) //
        .subscribe(sessions -> {
          dataSource.setSponsors(
              Data.<List<Sponsor>>builder().data(sessions).status(LOADED).build());
        });

    sponsorsResult.filter(Funcs.not(Results.isSuccessful())) //
        .compose(bindToLifecycle()) //
        .subscribe(sessionResult -> {
          dataSource.setSponsors(Data.<List<Sponsor>>builder().data(Lists.newArrayList())
              .status(ERROR)
              .throwable(sessionResult.error())
              .build());
        });

    onRequestSessions();
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);

    MenuItem licensesItem = menu.findItem(R.id.main_menu_licenses);
    licensesItem.setOnMenuItemClickListener(item -> {
      new LicensesDialog.Builder(this).setNotices(R.raw.notices).build().show();
      return true;
    });

    MenuItem appVersionItem = menu.findItem(R.id.main_menu_app_version);
    appVersionItem.setTitle(String.format("Version %s", BuildConfig.VERSION_NAME));

    return super.onCreateOptionsMenu(menu);
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

  @Override public void onRequestSessions() {
    dataSource.requestNewSessions();
    sessionsSubject.onNext(null);
  }

  @Override public void onRequestSponsors() {
    dataSource.requestNewSponsors();
    sponsorsSubject.onNext(null);
  }
}
