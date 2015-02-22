package org.selfconference.android.speakers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Speaker;

import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.Subscriber;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class SpeakerFragment extends BaseFragment implements SpeakerAdapter.OnSpeakerClickListener {
    public static final String TAG = SpeakerFragment.class.getName();

    @InjectView(R.id.speaker_recycler_view)
    RecyclerView speakerRecyclerView;

    @Inject
    SelfConferenceApi api;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(false);

    public SpeakerFragment() {
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

        addSubscription(
                api.getSpeakers()
                        .observeOn(mainThread())
                        .subscribe(speakersSubscriber)
        );
    }

    @Override
    public void onSpeakerClick(Speaker speaker) {
        Toast.makeText(getActivity(), speaker.getName(), Toast.LENGTH_SHORT).show();
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
}
