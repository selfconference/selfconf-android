package org.selfconference.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.Lists;
import org.selfconference.App;
import org.selfconference.BuildConfig;
import org.selfconference.R;
import org.selfconference.data.Data;
import org.selfconference.data.DataSource;
import org.selfconference.data.Funcs;
import org.selfconference.data.api.RestClient;
import org.selfconference.data.api.Results;
import org.selfconference.data.api.model.Session;
import org.selfconference.data.api.model.Sponsor;
import org.selfconference.ui.drawer.DrawerItem;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.psdev.licensesdialog.LicensesDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.adapter.rxjava2.Result;

import static org.selfconference.data.Data.Status.ERROR;
import static org.selfconference.data.Data.Status.LOADED;

public final class MainActivity extends BaseActivity implements FragmentCallbacks {

  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;

  @Inject ViewContainer viewContainer;
  @Inject DataSource dataSource;
  @Inject RestClient restClient;

  private final PublishSubject<Object> sessionsSubject = PublishSubject.create();
  private final PublishSubject<Object> sponsorsSubject = PublishSubject.create();
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private ActionBarDrawerToggle drawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App.context().getApplicationComponent().inject(this);

    ViewGroup container = viewContainer.forActivity(this);

    getLayoutInflater().inflate(R.layout.activity_main, container);
    ButterKnife.bind(this, container);

    setSupportActionBar(getToolbar());

    drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));

    drawerToggle = new ActionBarDrawerToggle(this,
        drawerLayout,
        getToolbar(),
        R.string.navigation_drawer_open,
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
        sessionsSubject.flatMap(__ -> restClient.getSessions()
                .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .share();

    Disposable sessionsSuccessful = sessionsResult.filter(Results.isSuccessful())
        .map(Results.responseBody())
        .compose(bindToLifecycle())
        .subscribe(sessions -> {
          dataSource.setSessions(
              Data.<List<Session>>builder().data(sessions).status(LOADED).build());
        });
    compositeDisposable.add(sessionsSuccessful);

    Disposable sessionsNotSuccessful = sessionsResult.filter(Funcs.not(Results.isSuccessful()))
        .compose(bindToLifecycle())
        .subscribe(sessionResult -> {
          dataSource.setSessions(Data.<List<Session>>builder().data(Lists.newArrayList())
              .status(ERROR)
              .throwable(sessionResult.error())
              .build());
        });
    compositeDisposable.add(sessionsNotSuccessful);

    onRequestSessions();

    Observable<Result<List<Sponsor>>> sponsorsResult =
        sessionsSubject.flatMap(__ -> restClient.getSponsors().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    Disposable sponsorsSuccessful = sponsorsResult.filter(Results.isSuccessful())
        .map(Results.responseBody())
        .compose(bindToLifecycle())
        .subscribe(sessions -> {
          dataSource.setSponsors(
              Data.<List<Sponsor>>builder().data(sessions).status(LOADED).build());
        });
    compositeDisposable.add(sponsorsSuccessful);

    Disposable sponsorsNotSuccessful = sponsorsResult.filter(Funcs.not(Results.isSuccessful()))
        .compose(bindToLifecycle())
        .subscribe(sponsorResult -> {
          dataSource.setSponsors(Data.<List<Sponsor>>builder().data(Lists.newArrayList())
              .status(ERROR)
              .throwable(sponsorResult.error())
              .build());
        });
    compositeDisposable.add(sponsorsNotSuccessful);

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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }

  private void clickNavigationDrawerItem(@IdRes int menuIdRes) {
    MenuItem clickedMenuItem = navigationView.getMenu().findItem(menuIdRes);
    navigationItemSelectedListener.onNavigationItemSelected(clickedMenuItem);
  }

  private void setupDrawerContent() {
    navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
  }

  private void changeFragment(DrawerItem drawerItem) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, drawerItem.fragment(), drawerItem.fragmentTag())
        .commit();
  }

  private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new NavigationView.OnNavigationItemSelectedListener() {
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
    sessionsSubject.onNext("Start Request Sessions");
  }

  @Override public void onRequestSponsors() {
    dataSource.requestNewSponsors();
    sponsorsSubject.onNext("Start Request Sponsors");
  }
}
