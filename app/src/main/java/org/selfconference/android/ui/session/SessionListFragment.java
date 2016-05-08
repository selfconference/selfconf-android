package org.selfconference.android.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import butterknife.BindView;
import com.google.common.base.Optional;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Funcs;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseListFragment;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.util.Instants;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public final class SessionListFragment extends BaseListFragment
    implements SwipeRefreshLayout.OnRefreshListener {
  private static final String EXTRA_DAY = "org.selfconference.android.ui.session.EXTRA_DAY";

  @BindView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject SessionPreferences sessionPreferences;
  @Inject RestClient restClient;

  private final PublishSubject<Day> sessionsSubject = PublishSubject.create();

  private Day day;
  private SessionAdapter sessionAdapter;

  public static SessionListFragment newInstance(Day day) {
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_DAY, day.name());
    SessionListFragment fragment = new SessionListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public SessionListFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

    String dayString = getArguments().getString(EXTRA_DAY);
    day = Day.valueOf(dayString);
    sessionAdapter = new SessionAdapter(sessionPreferences);
    sessionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        swipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    swipeRefreshLayout.setOnRefreshListener(this);

    sessionAdapter.setOnSessionClickListener(session -> {
      Intent intent = SessionDetailActivity.newIntent(getActivity(), session);
      getActivity().startActivity(intent);
    });

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    Observable<Result<List<Session>>> result =
        sessionsSubject.flatMap(sessions -> restClient.getSessions().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    result.filter(Results.isSuccessful())
        .map(res -> res.response().body())
        .flatMap((sessions) -> Observable.from(sessions) //
            .filter(session -> {
              Slot slot = Optional.fromNullable(session.slot()).or(Slot.empty());
              return Instants.areOnSameDay(slot.time(), day.getStartTime());
            }) //
            .toSortedList((session, session2) -> {
              Slot s1 = Optional.fromNullable(session.slot()).or(Slot.empty());
              Slot s2 = Optional.fromNullable(session2.slot()).or(Slot.empty());
              return s1.compareTo(s2);
            }))
        .compose(bindToLifecycle())
        .subscribe(sessions -> {
          Timber.d("Got filtered session: %s", sessions);
          sessionAdapter.setData(sessions);
        }, throwable -> Timber.e(throwable, "Y u no work"));

    result.filter(Funcs.not(Results.isSuccessful()))
        .compose(bindToLifecycle())
        .subscribe(sessionsResult -> {
          Timber.d(sessionsResult.error(), "Something happened here");
        });

    onRefresh();
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    if (sessionPreferences.hasFavorites()) {
      inflater.inflate(R.menu.favorites, menu);
      menu.findItem(R.id.action_favorites).setOnMenuItemClickListener(item -> {
        item.setChecked(!item.isChecked());
        sessionAdapter.filterFavorites(item.isChecked());
        return true;
      });
    }
  }

  @Override public void onResume() {
    super.onResume();
    sessionAdapter.refresh();
    refreshFavoritesMenu();
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule_item;
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return sessionAdapter;
  }

  private void refreshFavoritesMenu() {
    ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.invalidateOptionsMenu();
    }
  }

  @Override public void onRefresh() {
    getView().post(() -> {
      swipeRefreshLayout.setRefreshing(true);
      sessionsSubject.onNext(day);
    });
  }
}
