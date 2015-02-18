package org.selfconference.android.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.api.Day;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Subscriber;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class DaySessionFragment extends BaseFragment implements SessionsAdapter.Callback {
    private static final String KEY_DAY = "day";

    @Inject
    SelfConferenceApi api;

    @InjectView(R.id.schedule_item_recycler_view)
    RecyclerView scheduleItemRecyclerView;

    private final SessionsAdapter adapter = new SessionsAdapter(this);

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

        scheduleItemRecyclerView.setAdapter(adapter);
        scheduleItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addSubscription(
                api.getScheduleByDay(getDay())
                        .observeOn(mainThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_schedule_item;
    }

    @Override
    public void onSessionSelected(View view, Session session) {
        final Intent intent = SessionActivity.newIntent(getActivity(), session);
        final Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.transition_name_session)).toBundle();
        ActivityCompat.startActivity(getActivity(), intent, options);
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
            adapter.setSessions(sessions);
        }
    };
}
