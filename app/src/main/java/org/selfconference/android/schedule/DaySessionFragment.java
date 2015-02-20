package org.selfconference.android.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseFragment;
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

public class DaySessionFragment extends BaseFragment implements SessionsAdapter.OnSessionClickListener {
    private static final String KEY_DAY = "day";

    @Inject
    SelfConferenceApi api;

    @InjectView(R.id.schedule_item_recycler_view)
    RecyclerView scheduleItemRecyclerView;

    private final SessionsAdapter sessionsAdapter = new SessionsAdapter();

    public static DaySessionFragment newInstance(Day day) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DAY, day);
        final DaySessionFragment fragment = new DaySessionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public DaySessionFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionsAdapter.setOnSessionClickListener(this);

        scheduleItemRecyclerView.setAdapter(sessionsAdapter);
        scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addSubscription(
                bindFragment(this, api.getScheduleByDay(getDay())).subscribe(subscriber)
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        sessionsAdapter.refresh();
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_schedule_item;
    }

    @Override
    public void onSessionClick(SharedElements sharedElements, Session session) {
        final Intent intent = SessionDetailsActivity.newIntent(getActivity(), session);
        ActivityCompat.startActivity(getActivity(), intent, null);
    }

    private Day getDay() {
        return (Day) checkNotNull(getArguments().getSerializable(KEY_DAY));
    }

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
            sessionsAdapter.setSessions(sessions);
        }
    };
}
