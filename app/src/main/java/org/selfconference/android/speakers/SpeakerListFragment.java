package org.selfconference.android.speakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import javax.inject.Inject;
import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.session.SessionDetailsActivity;
import rx.Observable;
import timber.log.Timber;

import static org.selfconference.android.utils.rx.Transformers.ioSchedulers;
import static org.selfconference.android.utils.rx.Transformers.setRefreshing;

public class SpeakerListFragment extends BaseListFragment
    implements SpeakerAdapter.OnSpeakerClickListener, SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = SpeakerListFragment.class.getName();

  @Bind(R.id.speaker_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;

  @Inject Api api;

  private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(false);

  public SpeakerListFragment() {
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    speakerAdapter.setOnSpeakerClickListener(this);

    speakerRecyclerView.setAdapter(speakerAdapter);
    speakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    swipeRefreshLayout.setOnRefreshListener(this);

    fetchData();
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_speaker;
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return speakerAdapter;
  }

  @Override public void onSpeakerClick(Speaker speaker) {
    Timber.d("Speaker clicked: %s", speaker);
    Observable.from(speaker.getSessions())
        .first()
        .flatMap(session -> api.getSessionById(session.getId()))
        .compose(setRefreshing(swipeRefreshLayout))
        .compose(bindToLifecycle())
        .compose(ioSchedulers())
        .subscribe(session -> {
          final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
          startActivity(intent);
        }, throwable -> {
          Timber.e(throwable, "Failed to load sessions for speaker");
        });
  }

  @Override public void onRefresh() {
    fetchData();
  }

  private void fetchData() {
    api.getSpeakers() //
        .compose(setRefreshing(swipeRefreshLayout)) //
        .compose(bindToLifecycle()) //
        .compose(ioSchedulers()) //
        .subscribe(speakerAdapter::setData, //
            throwable -> {
              Timber.e(throwable, "Failed to load speakers");
            });
  }
}
