package org.selfconference.android.speakers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.InjectView;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.api.ApiRequestSubscriber;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.utils.rx.Transformers;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

import static rx.android.app.AppObservable.bindFragment;

public class SpeakerListFragment extends BaseListFragment
    implements SpeakerAdapter.OnSpeakerClickListener, SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = SpeakerListFragment.class.getName();

  @InjectView(R.id.speaker_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
  @InjectView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;

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
    final Observable<Session> sessionObservable = Observable.from(speaker.getSessions())
        .first()
        .flatMap(new Func1<Session, Observable<Session>>() {
          @Override public Observable<Session> call(Session session) {
            return api.getSessionById(session.getId());
          }
        });
    addSubscription(bindFragment(this, sessionObservable) //
        .compose(Transformers.<Session>setRefreshing(swipeRefreshLayout))
        .subscribe(new SpeakerClickSubscriber(getActivity())));
  }

  @Override public void onRefresh() {
    fetchData();
  }

  private void fetchData() {
    final Observable<List<Speaker>> speakersObservable = bindFragment(this, api.getSpeakers()) //
        .compose(Transformers.<List<Speaker>>setRefreshing(swipeRefreshLayout));
    addSubscription(bindFragment(this, speakersObservable) //
        .subscribe(new SpeakerListSubscriber(speakerAdapter)));
  }

  private static final class SpeakerListSubscriber extends ApiRequestSubscriber<List<Speaker>> {

    private final WeakReference<SpeakerAdapter> speakerAdapter;

    public SpeakerListSubscriber(SpeakerAdapter speakerAdapter) {
      super();
      this.speakerAdapter = new WeakReference<>(speakerAdapter);
    }

    @Override public void onError(Throwable e) {
      super.onError(e);
      Timber.e(e, "Failed to load speakers");
    }

    @Override public void onNext(List<Speaker> speakers) {
      final SpeakerAdapter speakerAdapter = this.speakerAdapter.get();
      if (speakerAdapter != null) {
        speakerAdapter.setData(speakers);
      }
    }
  }

  private static final class SpeakerClickSubscriber extends ApiRequestSubscriber<Session> {

    private final WeakReference<Activity> activityWeakReference;

    public SpeakerClickSubscriber(Activity activity) {
      super();
      this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override public void onError(Throwable e) {
      super.onError(e);
      Timber.e(e, "Failed to load sessions for speaker");
    }

    @Override public void onNext(Session session) {
      final Activity activity = activityWeakReference.get();
      if (activity != null) {
        final Intent intent = SessionDetailsActivity.newIntent(activity, session);
        activity.startActivity(intent);
      }
    }
  }
}
