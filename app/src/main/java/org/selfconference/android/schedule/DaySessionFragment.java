package org.selfconference.android.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import timber.log.Timber;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class DaySessionFragment extends Fragment implements SessionsAdapter.Callback {

    @InjectView(R.id.schedule_item_recycler_view)
    RecyclerView scheduleItemRecyclerView;

    private final SessionsAdapter adapter = new SessionsAdapter(this);
    private final Subscriber<List<Session>> subscriber = new Subscriber<List<Session>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "Schedule failed to load");
        }

        @Override
        public void onNext(List<Session> sessions) {
            adapter.setSessions(sessions);
        }
    };

    public DaySessionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        scheduleItemRecyclerView.setAdapter(adapter);
        scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new SelfConferenceApi().loadSchedule()
                .observeOn(mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void onSessionSelected(View view, Session session) {
        final Intent intent = SessionActivity.newIntent(getActivity(), session);
        final Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.transition_name_session)).toBundle();
        ActivityCompat.startActivity(getActivity(), intent, options);
    }
}
