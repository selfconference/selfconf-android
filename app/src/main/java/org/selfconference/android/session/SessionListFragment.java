package org.selfconference.android.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Day;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.utils.SharedElements;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Subscriber;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static rx.android.app.AppObservable.bindFragment;

public class SessionListFragment extends BaseListFragment implements SessionAdapter.OnSessionClickListener {
    private static final String EXTRA_DAY = "org.selfconference.android.session.EXTRA_DAY";

    @InjectView(R.id.schedule_item_recycler_view) RecyclerView scheduleItemRecyclerView;

    @Inject SelfConferenceApi api;

    private final SessionAdapter sessionAdapter = new SessionAdapter();

    public static SessionListFragment newInstance(Day day) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DAY, day);
        final SessionListFragment fragment = new SessionListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SessionListFragment() {
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionAdapter.setOnSessionClickListener(this);

        scheduleItemRecyclerView.setAdapter(sessionAdapter);
        scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final Day day = checkNotNull((Day) getArguments().getSerializable(EXTRA_DAY));
        addSubscription(
                bindFragment(this, api.getSessionsByDay(day)).subscribe(subscriber)
        );
    }

    @Override public void onResume() {
        super.onResume();
        sessionAdapter.refresh();
    }

    @Override protected int layoutResId() {
        return R.layout.fragment_schedule_item;
    }

    @Override protected FilterableAdapter getFilterableAdapter() {
        return sessionAdapter;
    }

    @Override public void onSessionClick(SharedElements sharedElements, Session session) {
        final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
        ActivityCompat.startActivity(getActivity(), intent, null);
    }

    private final Subscriber<List<Session>> subscriber = new Subscriber<List<Session>>() {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {
            Timber.e(e, "Schedule failed to load");
        }

        @Override public void onNext(List<Session> sessions) {
            sessionAdapter.setData(sessions);
        }
    };
}
