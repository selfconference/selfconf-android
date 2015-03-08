package org.selfconference.android.speakers;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.api.Speaker;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SimpleSessionAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkState;
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
        final Observable<List<Session>> sessionObservable = api.getSessionsForSpeaker(speaker);
        addSubscription(
                bindFragment(this, sessionObservable).subscribe(new SessionObservableSubscriber())
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

    private final class SessionObservableSubscriber extends Subscriber<List<Session>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "Session not found");
        }

        @Override
        public void onNext(List<Session> sessions) {
            Timber.d("Speaker's sessions: %s", Arrays.toString(sessions.toArray()));
            checkState(!sessions.isEmpty(), "Speaker must have at least one session");

            if (sessions.size() == 1) {
                startSessionDetailsActivity(sessions.get(0));
            } else {
                final SimpleSessionAdapter adapter = new SimpleSessionAdapter(getActivity(), sessions);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose session")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialog, int which) {
                                dialog.dismiss();
                                final Session session = adapter.getItem(which);
                                startSessionDetailsActivity(session);
                            }
                        })
                        .show();
            }
        }

        private void startSessionDetailsActivity(Session session) {
            final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
            startActivity(intent);
        }
    }

}
