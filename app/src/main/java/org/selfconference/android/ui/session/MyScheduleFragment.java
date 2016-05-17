package org.selfconference.android.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle.FragmentEvent;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.Funcs;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.Sessions;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.util.Instants;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MyScheduleFragment extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = MyScheduleFragment.class.getName();

  @BindView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

  @Inject DataSource dataSource;
  @Inject SessionPreferences sessionPreferences;

  private SessionAdapter sessionAdapter;

  public static Fragment newInstance() {
    Bundle args = new Bundle();

    MyScheduleFragment fragment = new MyScheduleFragment();
    fragment.setArguments(args);

    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

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
      new AlertDialog.Builder(getActivity()) //
          .setMessage("Remove session from your schedule?") //
          .setPositiveButton("Remove", (dialog, which) -> {
            sessionPreferences.unfavorite(session);
            dataSource.tickleSessions();
            Snackbar.make(view, "Removed from schedule", Snackbar.LENGTH_SHORT) //
                .setAction("Undo", v -> {
                  sessionPreferences.favorite(session);
                  dataSource.tickleSessions();
                }) //
                .show();
          }) //
          .setNegativeButton("Cancel", null) //
          .show();
    });

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override public void onStart() {
    super.onStart();

    dataSource.favoriteSessions()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindUntilEvent(FragmentEvent.STOP))
        .flatMap(sessions -> Observable.from(sessions) //
            .toSortedList((session, session2) -> {
              Slot s1 = Optional.fromNullable(session.slot()).or(Slot.empty());
              Slot s2 = Optional.fromNullable(session2.slot()).or(Slot.empty());
              return s1.compareTo(s2);
            })) //
        .map(Sessions.groupBySlotTime()) //
        .flatMap(sessions -> Observable.from(sessions.asMap().entrySet()) //
            .map(entry -> {
              ImmutableList.Builder<SessionAdapter.ViewModel> models = ImmutableList.builder();
              models.add(SessionAdapter.Header.withText(Instants.dayTimeString(entry.getKey())));
              models.addAll(Collections2.transform(entry.getValue(), session -> {
                List<Speaker> speakers =
                    Optional.fromNullable(session.speakers()).or(ImmutableList.of());
                List<String> speakerNames = Lists.transform(speakers, Speaker::name);
                Room room = Optional.fromNullable(session.room()).or(Room.empty());
                return SessionAdapter.ListItem.builder()
                    .session(session)
                    .lineOne(session.name())
                    .lineTwo(Joiner.on(" / ").join(speakerNames))
                    .lineThree(room.name())
                    .build();
              }));
              return models.build();
            }) //
            .concatMapIterable(Funcs.identity()) //
            .toList()) //
        .subscribe(viewModels -> {
          sessionAdapter.setViewModels(viewModels);
        });
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule_item;
  }

  @Override public void onRefresh() {
    swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 200);
  }
}
