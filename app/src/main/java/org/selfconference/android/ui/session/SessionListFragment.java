package org.selfconference.android.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Funcs;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.util.Instants;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public final class SessionListFragment extends BaseFragment implements OnRefreshListener {
  private static final String EXTRA_DAY = "org.selfconference.android.ui.session.EXTRA_DAY";

  @BindView(R.id.schedule_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

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

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    Observable<Result<List<Session>>> result =
        sessionsSubject.flatMap(__ -> restClient.getSessions().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    result.filter(Results.isSuccessful())
        .map(Results.responseBody())
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
        .map(sessions -> Multimaps.index(sessions, session -> {
          return Optional.fromNullable(session.slot()).or(Slot.empty()).time();
        }))
        .flatMap(sessions -> {
          return Observable.from(sessions.asMap().entrySet()) //
              .map(entry -> {
                ImmutableList.Builder<SessionAdapter.ViewModel> models = ImmutableList.builder();
                models.add(SessionAdapter.Header.withText(Instants.miniTimeString(entry.getKey())));
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
              })
              .concatMapIterable(Funcs.identity())
              .toList();
        })
        .compose(bindToLifecycle())
        .subscribe(viewModels -> {
          sessionAdapter.setViewModels(viewModels);
        });

    result.filter(Funcs.not(Results.isSuccessful()))
        .compose(bindToLifecycle())
        .subscribe(sessionsResult -> {
          Timber.d(sessionsResult.error(), "Something happened here");
        });

    onRefresh();
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_schedule_item;
  }

  @Override public void onRefresh() {
    swipeRefreshLayout.post(() -> {
      swipeRefreshLayout.setRefreshing(true);
      sessionsSubject.onNext(day);
    });
  }
}
