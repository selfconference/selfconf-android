package org.selfconference.android.ui.session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.trello.rxlifecycle.FragmentEvent;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Data;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.DataTransformers;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.Sessions;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.ui.FragmentCallbacks;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class SessionListFragment extends BaseFragment implements OnRefreshListener {
  private static final String EXTRA_DAY = "org.selfconference.android.ui.session.EXTRA_DAY";

  @BindView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject RestClient restClient;
  @Inject DataSource dataSource;
  @Inject SessionPreferences sessionPreferences;

  private Day day;
  private SessionAdapter sessionAdapter;

  private FragmentCallbacks callbacks;

  public static SessionListFragment newInstance(Day day) {
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_DAY, day.name());
    SessionListFragment fragment = new SessionListFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public SessionListFragment() {
  }

  @Override public void onAttach(Activity activity) {
    try {
      callbacks = (FragmentCallbacks) activity;
    } catch (ClassCastException e) {
      throw new RuntimeException(e);
    }
    super.onAttach(activity);
  }

  @Override public void onDetach() {
    super.onDetach();
    callbacks = null;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

    String dayString = getArguments().getString(EXTRA_DAY);
    day = Day.valueOf(dayString);
    sessionAdapter = new SessionAdapter();
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

    sessionAdapter.setOnSessionLongClickListener(session -> {
      if (sessionPreferences.isFavorite(session)) {
        new AlertDialog.Builder(getActivity()) //
            .setMessage(R.string.prompt_schedule_remove) //
            .setPositiveButton(R.string.remove, (dialog, which) -> {
              sessionPreferences.unfavorite(session);
              dataSource.tickleSessions();
              Snackbar.make(view, R.string.message_schedule_remove, Snackbar.LENGTH_SHORT) //
                  .setAction(R.string.undo, v -> {
                    sessionPreferences.favorite(session);
                    dataSource.tickleSessions();
                  }) //
                  .show();
            }) //
            .setNegativeButton(R.string.cancel, null) //
            .show();
      } else {
        new AlertDialog.Builder(getActivity()) //
            .setMessage(R.string.prompt_schedule_add) //
            .setPositiveButton(R.string.add, (dialog, which) -> {
              sessionPreferences.favorite(session);
              dataSource.tickleSessions();
              Snackbar.make(view, R.string.message_schedule_add, Snackbar.LENGTH_SHORT) //
                  .setAction(R.string.undo, v -> {
                    sessionPreferences.unfavorite(session);
                    dataSource.tickleSessions();
                  }) //
                  .show();
            }) //
            .setNegativeButton(R.string.cancel, null) //
            .show();
      }
    });

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override public void onStart() {
    super.onStart();

    Observable<Data<List<Session>>> sessionsData = dataSource.sessions()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindUntilEvent(FragmentEvent.STOP));

    sessionsData.compose(DataTransformers.loading()) //
        .subscribe(__ -> {
          swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        });

    sessionsData.compose(DataTransformers.loaded()) //
        .flatMap(Sessions.sortedForDay(day)) //
        .map(Sessions.groupBySlotTime()) //
        .flatMap(Sessions.toViewModels()) //
        .subscribe(viewModels -> {
          sessionAdapter.setViewModels(viewModels);
        });

    sessionsData.compose(DataTransformers.error()) //
        .subscribe(throwable -> {
          Timber.e(throwable, "Something happened here");
        });
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule_item;
  }

  @Override public void onRefresh() {
    if (callbacks != null) {
      callbacks.onRequestSessions();
    }
  }
}
