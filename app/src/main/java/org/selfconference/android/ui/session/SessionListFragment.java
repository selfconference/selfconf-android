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
import butterknife.Bind;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseListFragment;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.data.event.GetSessionsAddEvent;
import org.selfconference.android.data.event.GetSessionsSuccessEvent;
import org.selfconference.android.data.job.GetSessionsJob;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

public final class SessionListFragment extends BaseListFragment {

  @Bind(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject SessionPreferences sessionPreferences;
  @Inject JobManager jobManager;
  @Inject EventBus eventBus;

  private final SessionAdapter sessionAdapter = new SessionAdapter();

  public static SessionListFragment newInstance() {
    Bundle bundle = new Bundle();

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
    eventBus.register(this);
    sessionAdapter.refresh();
    refreshFavoritesMenu();
  }

  @Override public void onPause() {
    super.onPause();
    eventBus.unregister(this);
  }

  @Subscribe(threadMode = MAIN) public void onGetSessionsAdded(GetSessionsAddEvent event) {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Subscribe(threadMode = MAIN) public void onGetSessionsSucceeded(GetSessionsSuccessEvent event) {
    swipeRefreshLayout.setRefreshing(false);
    sessionAdapter.setData(event.sessions);
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule_item;
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return sessionAdapter;
  }

  private void fetchData() {
    jobManager.addJobInBackground(new GetSessionsJob());
  }

  private void refreshFavoritesMenu() {
    ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.invalidateOptionsMenu();
    }
  }
}
