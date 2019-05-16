package org.selfconference.android.ui.session;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle.FragmentEvent;
import org.selfconference.android.R;
import org.selfconference.android.data.Data;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.DataTransformers;
import org.selfconference.android.data.Funcs;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.Sessions;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.ui.misc.BetterViewAnimator;
import org.selfconference.android.util.Instants;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MyScheduleFragment extends BaseFragment {
  public static final String TAG = MyScheduleFragment.class.getName();

  @BindView(R.id.animator_view) BetterViewAnimator animatorView;
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
        animatorView.setDisplayedChildId(determineViewToDisplay());
      }
    });
  }

  @IdRes private int determineViewToDisplay() {
    if (sessionAdapter.getItemCount() == 0) {
      return R.id.session_empty_view;
    }
    return R.id.schedule_item_recycler_view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    animatorView.setDisplayedChildId(R.id.session_initial_view);

    sessionAdapter.setOnSessionClickListener(session -> {
      Intent intent = SessionDetailActivity.newIntent(getActivity(), session);
      getActivity().startActivity(intent);
    });

    sessionAdapter.setOnSessionLongClickListener(session -> {
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
    });

    scheduleItemRecyclerView.setAdapter(sessionAdapter);
    scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }

  @Override public void onStart() {
    super.onStart();

    Observable<Data<List<Session>>> sessionsResult = dataSource.sessions()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    sessionsResult.compose(DataTransformers.loaded())
        .flatMap(sessions1 -> Observable.from(sessions1) //
            .filter(sessionPreferences::isFavorite) //
            .toList())
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

    sessionsResult.compose(DataTransformers.error()).subscribe(data -> {
      animatorView.setDisplayedChildId(R.id.session_error_view);
    });
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_my_schedule;
  }
}
