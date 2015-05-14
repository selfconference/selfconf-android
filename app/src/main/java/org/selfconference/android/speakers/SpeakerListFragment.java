package org.selfconference.android.speakers;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.utils.rx.Transformers;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

import static rx.android.app.AppObservable.bindFragment;

public class SpeakerListFragment extends BaseListFragment implements SpeakerAdapter.OnSpeakerClickListener {
    public static final String TAG = SpeakerListFragment.class.getName();

    @InjectView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
    @InjectView(R.id.progress_bar) ProgressBar progressBar;

    @Inject Api api;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(false);

    public SpeakerListFragment() {
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        speakerAdapter.setOnSpeakerClickListener(this);

        speakerRecyclerView.setAdapter(speakerAdapter);
        speakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Observable<List<Speaker>> speakersObservable = bindFragment(this, api.getSpeakers())
                .compose(Transformers.<List<Speaker>>showAndHideProgressBar(progressBar));
        addSubscription(
                bindFragment(this, speakersObservable).subscribe(speakersSubscriber)
        );
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
        addSubscription(
                bindFragment(this, sessionObservable)
                        .compose(Transformers.<Session>showAndHideProgressBar(progressBar))
                        .subscribe(new SpeakerClickSubscriber(getActivity()))
        );
    }

    private final Subscriber<List<Speaker>> speakersSubscriber = new Subscriber<List<Speaker>>() {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {
            Timber.e(e, "Failed to load speakers");
        }

        @Override public void onNext(List<Speaker> speakers) {
            speakerAdapter.setData(speakers);
        }
    };

    private static final class SpeakerClickSubscriber extends Subscriber<Session> {

        private final WeakReference<Activity> activityWeakReference;

        public SpeakerClickSubscriber(Activity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {

        }

        @Override public void onNext(Session session) {
            final Activity activity = activityWeakReference.get();
            final Intent intent = SessionDetailsActivity.newIntent(activity, session);
            activity.startActivity(intent);
        }
    }

}
