package org.selfconference.android.speakers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.api.Speaker;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SimpleSessionAdapter;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

import static android.content.DialogInterface.OnClickListener;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_speaker_list, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                speakerAdapter.filter(s);
                return true;
            }
        });
        searchView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    speakerAdapter.reset();
                }
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSpeakerClick(Speaker speaker) {
        Timber.d("Speaker clicked: %s", speaker.toString());
        final Observable<List<Session>> sessionObservable = api.getSessionsForSpeaker(speaker);
        addSubscription(
                bindFragment(this, sessionObservable).subscribe(new SessionObservableSubscriber(getActivity()))
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
            speakerAdapter.setData(speakers);
        }
    };

    private static final class SessionObservableSubscriber extends Subscriber<List<Session>> {

        private final WeakReference<Activity> activityReference;

        private SessionObservableSubscriber(Activity activity) {
            super();
            this.activityReference = new WeakReference<>(activity);
        }

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
                final Activity activity = activityReference.get();
                final SimpleSessionAdapter adapter = new SimpleSessionAdapter(activity, sessions);
                new AlertDialog.Builder(activity)
                        .setTitle("Choose session")
                        .setAdapter(adapter, new OnClickListener() {
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
            final Activity activity = this.activityReference.get();
            final Intent intent = SessionDetailsActivity.newIntent(activity, session);
            activity.startActivity(intent);
        }
    }

}
