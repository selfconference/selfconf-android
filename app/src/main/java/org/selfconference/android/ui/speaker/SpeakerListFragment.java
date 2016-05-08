package org.selfconference.android.ui.speaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.ui.BaseListFragment;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.session.SessionDetailActivity;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public final class SpeakerListFragment extends BaseListFragment implements OnRefreshListener {
  public static final String TAG = SpeakerListFragment.class.getName();

  @BindView(R.id.speaker_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;

  @Inject Picasso picasso;
  @Inject RestClient restClient;

  private final PublishSubject<Void> speakersSubject = PublishSubject.create();
  private final PublishSubject<Integer> sessionSubject = PublishSubject.create();

  private SpeakerAdapter speakerAdapter;

  public SpeakerListFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

    speakerAdapter = new SpeakerAdapter(picasso, false);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    speakerAdapter.setOnSpeakerClickListener(speaker -> {
      Timber.d("Speaker clicked: %s", speaker);
      List<Session> sessions = Optional.fromNullable(speaker.sessions()).or(ImmutableList.of());
      if (sessions.isEmpty()) {
        // TODO handle empty state
      } else {
        // TODO handle possibility where speaker has more than one session
        Session session = sessions.get(0);

        // TODO move this to SessionDetailActivity?
        Observable<Result<Session>> result =
            sessionSubject.flatMap(id -> restClient.getSessionById(id).subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        result.filter(Results.isSuccessful())
            .map(Results.responseBody())
            .compose(bindToLifecycle())
            .subscribe(sessionById -> {
              swipeRefreshLayout.setRefreshing(false);
              Intent intent = SessionDetailActivity.newIntent(getActivity(), session);
              startActivity(intent);
            });

        // Fetch session for id.
        getView().post(() -> {
          swipeRefreshLayout.setRefreshing(true);
          sessionSubject.onNext(session.id());
        });
      }
    });

    speakerRecyclerView.setAdapter(speakerAdapter);
    speakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    speakerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        swipeRefreshLayout.setRefreshing(false);
      }
    });

    swipeRefreshLayout.setOnRefreshListener(this);

    Observable<Result<List<Speaker>>> result =
        speakersSubject.flatMap(__ -> restClient.getSpeakers().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .share();

    result.filter(Results.isSuccessful())
        .map(Results.responseBody())
        .compose(bindToLifecycle())
        .subscribe(speakers -> {
          speakerAdapter.setData(speakers);
        });

    onRefresh();
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_speaker;
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return speakerAdapter;
  }

  @Override public void onRefresh() {
    getView().post(() -> {
      swipeRefreshLayout.setRefreshing(true);
      speakersSubject.onNext(null);
    });
  }
}
