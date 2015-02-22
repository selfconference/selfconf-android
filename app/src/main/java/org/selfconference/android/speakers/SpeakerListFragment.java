package org.selfconference.android.speakers;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.api.Speaker;
import org.selfconference.android.session.SessionDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

import static rx.android.app.AppObservable.bindFragment;

public class SpeakerListFragment extends BaseFragment implements SpeakerAdapter.OnSpeakerClickListener {
    public static final String TAG = SpeakerListFragment.class.getName();

    @InjectView(R.id.speaker_recycler_view)
    RecyclerView speakerRecyclerView;

    @Inject
    SelfConferenceApi api;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(false);

    public SpeakerListFragment() {
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_speaker;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        speakerAdapter.setOnSpeakerClickListener(this);

        speakerRecyclerView.setAdapter(speakerAdapter);
        speakerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Observable<List<Speaker>> speakersObservable = api.getSpeakers();
        addSubscription(
                bindFragment(this, speakersObservable).subscribe(speakersSubscriber)
        );
    }

    @Override
    public void onSpeakerClick(Speaker speaker) {
        Timber.d("Speaker clicked: %s", speaker.toString());
        final Observable<Session> sessionObservable = api.getSessionForSpeaker(speaker);
        addSubscription(
                bindFragment(this, sessionObservable).subscribe(sessionSubscriber)
        );
    }

    private final Subscriber<List<Speaker>> speakersSubscriber = new Subscriber<List<Speaker>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Speaker> speakers) {
            speakerAdapter.setSpeakers(speakers);
        }
    };

    private final Subscriber<Session> sessionSubscriber = new Subscriber<Session>() {
        @Override
        public void onCompleted() {
            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "Session not found");
        }

        @Override
        public void onNext(Session session) {
            final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
            startActivity(intent);
        }
    };
}
