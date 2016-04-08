package org.selfconference.android.speakers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.birbit.android.jobqueue.JobManager;
import com.google.common.collect.ImmutableList;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.data.events.GetSessionAddEvent;
import org.selfconference.android.data.events.GetSessionSuccessEvent;
import org.selfconference.android.data.events.GetSpeakersAddEvent;
import org.selfconference.android.data.events.GetSpeakersSuccessEvent;
import org.selfconference.android.data.jobs.GetSessionJob;
import org.selfconference.android.data.jobs.GetSpeakersJob;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionDetailsActivity;
import timber.log.Timber;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

public final class SpeakerListFragment extends BaseListFragment {
  public static final String TAG = SpeakerListFragment.class.getName();

  @Bind(R.id.speaker_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @Bind(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;

  @Inject Api api;
  @Inject JobManager jobManager;
  @Inject EventBus eventBus;

  private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(false);

  public SpeakerListFragment() {
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    speakerAdapter.setOnSpeakerClickListener(speaker -> {
      Timber.d("Speaker clicked: %s", speaker);
      ImmutableList<Session> sessions = speaker.sessions();
      if (sessions.isEmpty()) {
        // TODO handle empty state
      } else {
        // TODO handle possibility where speaker has more than one session
        jobManager.addJobInBackground(GetSessionJob.create(sessions.get(0).id()));
      }
    });

    speakerRecyclerView.setAdapter(speakerAdapter);
    speakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    swipeRefreshLayout.setOnRefreshListener(this::fetchData);

    fetchData();
  }

  @Override public void onResume() {
    super.onResume();
    eventBus.register(this);
  }

  @Override public void onPause() {
    super.onPause();
    eventBus.unregister(this);
  }

  @Subscribe(threadMode = MAIN) public void onGetSessionAdded(GetSessionAddEvent event) {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Subscribe(threadMode = MAIN) public void onGetSessionSucceeded(GetSessionSuccessEvent event) {
    swipeRefreshLayout.setRefreshing(false);
    Intent intent = SessionDetailsActivity.newIntent(getActivity(), event.session);
    startActivity(intent);
  }

  @Subscribe(threadMode = MAIN) public void onGetSpeakersAdded(GetSpeakersAddEvent event) {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Subscribe(threadMode = MAIN) public void onGetSpeakersSucceeded(GetSpeakersSuccessEvent event) {
    swipeRefreshLayout.setRefreshing(false);
    speakerAdapter.setData(event.speakers);
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_speaker;
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return speakerAdapter;
  }

  private void fetchData() {
    jobManager.addJobInBackground(GetSpeakersJob.create());
  }
}
