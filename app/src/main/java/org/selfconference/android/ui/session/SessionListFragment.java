package org.selfconference.android.ui.session;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.snackbar.Snackbar;
import com.trello.rxlifecycle3.android.FragmentEvent;
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
import org.selfconference.android.ui.misc.BetterViewAnimator;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class SessionListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
  private static final String EXTRA_DAY = "org.selfconference.android.ui.session.EXTRA_DAY";

  @BindView(R.id.animator_view) BetterViewAnimator animatorView;
  @BindView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject RestClient restClient;
  @Inject DataSource dataSource;
  @Inject SessionPreferences sessionPreferences;

  private Day day;
  private SessionAdapter sessionAdapter;
  private FragmentCallbacks callbacks;
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        animatorView.setDisplayedChildId(determineViewToDisplay());
        swipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  @IdRes private int determineViewToDisplay() {
    if (sessionAdapter.getItemCount() == 0) {
      return R.id.session_empty_view;
    }
    return R.id.schedule_swipe_refresh_layout;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    animatorView.setDisplayedChildId(R.id.session_initial_view);

    swipeRefreshLayout.setOnRefreshListener(this);

    sessionAdapter.setOnSessionClickListener(session -> {
      Intent intent = SessionDetailActivity.newIntent(getActivity(), session);
      getActivity().startActivity(intent);
    });

    sessionAdapter.setOnSessionLongClickListener(session -> {
      if (sessionPreferences.isFavorite(session)) {
        new AlertDialog.Builder(getActivity())
            .setMessage(R.string.prompt_schedule_remove)
            .setPositiveButton(R.string.remove, (dialog, which) -> {
              sessionPreferences.unfavorite(session);
              dataSource.tickleSessions();
              Snackbar.make(view, R.string.message_schedule_remove, Snackbar.LENGTH_SHORT)
                  .setAction(R.string.undo, v -> {
                    sessionPreferences.favorite(session);
                    dataSource.tickleSessions();
                  })
                  .show();
            })
            .setNegativeButton(R.string.cancel, null)
            .show();
      } else {
        new AlertDialog.Builder(getActivity())
            .setMessage(R.string.prompt_schedule_add)
            .setPositiveButton(R.string.add, (dialog, which) -> {
              sessionPreferences.favorite(session);
              dataSource.tickleSessions();
              Snackbar.make(view, R.string.message_schedule_add, Snackbar.LENGTH_SHORT)
                  .setAction(R.string.undo, v -> {
                    sessionPreferences.unfavorite(session);
                    dataSource.tickleSessions();
                  })
                  .show();
            })
            .setNegativeButton(R.string.cancel, null)
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

    Disposable sessionsLoading = sessionsData.compose(DataTransformers.loading())
        .subscribe(__ -> swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true)));
    compositeDisposable.add(sessionsLoading);

    Disposable sessionsLoaded = sessionsData.compose(DataTransformers.loaded())
        .flatMapSingle(Sessions.sortedForDay(day))
        .map(Sessions.groupBySlotTime())
        .flatMapSingle(Sessions.toViewModels())
        .subscribe(viewModels -> sessionAdapter.setViewModels(viewModels));
    compositeDisposable.add(sessionsLoaded);

    Disposable sessionsError = sessionsData.compose(DataTransformers.error())
        .subscribe(throwable -> {
          animatorView.setDisplayedChildId(R.id.session_error_view);
          swipeRefreshLayout.setRefreshing(false);
          Timber.e(throwable, "Something happened here");
        });
    compositeDisposable.add(sessionsError);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_sessions;
  }

  @OnClick(R.id.session_error_refresh_button) void onErrorRefreshRequested() {
    onRefresh();
  }

  @Override public void onRefresh() {
    if (callbacks != null) {
      callbacks.onRequestSessions();
    }
  }
}
