package org.selfconference.android.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import butterknife.Bind;
import javax.inject.Inject;
import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.rx.Transformers.ioSchedulers;
import static org.selfconference.android.utils.rx.Transformers.setRefreshing;

public final class SessionListFragment extends BaseListFragment {
  private static final String EXTRA_DAY =
      "org.selfconference.android.session.SessionListFragment.EXTRA_DAY";

  @Bind(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject Api api;
  @Inject SessionPreferences sessionPreferences;

  private final SessionAdapter sessionAdapter = new SessionAdapter();
  private Day day;

  public static SessionListFragment newInstance(@NonNull Day day) {
    checkNotNull(day, "day == null");

    Bundle bundle = new Bundle(1);
    bundle.putSerializable(EXTRA_DAY, day);

    SessionListFragment fragment = new SessionListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public SessionListFragment() {
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    swipeRefreshLayout.setOnRefreshListener(this::fetchData);

    sessionAdapter.setOnSessionClickListener(session -> {
      Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
      getActivity().startActivity(intent);
    });

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    day = checkNotNull((Day) getArguments().getSerializable(EXTRA_DAY));

    fetchData();
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

  private void fetchData() {
    api.getSessionsByDay(day) //
        .compose(setRefreshing(swipeRefreshLayout)) //
        .compose(bindToLifecycle()) //
        .compose(ioSchedulers()) //
        .subscribe(sessionAdapter::setData, //
            throwable -> {
              Timber.e(throwable, "Schedule failed to load");
            });
  }

  private void refreshFavoritesMenu() {
    ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.invalidateOptionsMenu();
    }
  }
}
