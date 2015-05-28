package org.selfconference.android.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.selfconference.android.App;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.feedback.FeedbackFragment;
import org.selfconference.android.feedback.SuccessfulFeedbackSubmission;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerAdapter.OnSpeakerClickListener;
import org.selfconference.android.utils.Intents;
import org.selfconference.android.utils.NestedLinearLayoutManager;
import org.selfconference.android.views.FloatingActionButton;
import org.selfconference.android.views.FloatingActionButton.OnCheckedChangeListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

import static android.text.Html.fromHtml;
import static butterknife.ButterKnife.Setter;
import static butterknife.ButterKnife.apply;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.DateStringer.toDateString;

public class SessionDetailsActivity extends BaseActivity implements OnSpeakerClickListener, OnCheckedChangeListener {
    private static final String EXTRA_SESSION = "org.selfconference.android.session.SESSION";
    private static final Setter<TextView, Integer> TEXT_COLOR_SETTER = new Setter<TextView, Integer>() {
        @Override public void set(TextView view, Integer value, int index) {
            view.setTextColor(value);
        }
    };

    @InjectView(R.id.long_title) TextView sessionTitle;
    @InjectView(R.id.scroll_view) ScrollView scrollView;
    @InjectView(R.id.speakers_header) TextView speakersHeader;
    @InjectView(R.id.favorite_button) FloatingActionButton favoriteButton;
    @InjectView(R.id.session_detail_recycler_view) RecyclerView sessionDetailRecyclerView;
    @InjectView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
    @InjectView(R.id.submit_feedback) TextView submitFeedback;
    @InjectViews({R.id.speakers_header, R.id.more_header}) List<TextView> headers;

    @Inject SessionPreferences preferences;
    @Inject Bus bus;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(true);

    private Session session;

    public static Intent newIntent(final Context context, final Session session) {
        return new Intent(context, SessionDetailsActivity.class).putExtra(EXTRA_SESSION, session);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        App.getInstance().inject(this);
        ButterKnife.inject(this);

        session = checkNotNull((Session) getIntent().getParcelableExtra(EXTRA_SESSION));

        setUpActionBar();

        sessionTitle.setText(session.getTitle());
        favoriteButton.setChecked(preferences.isFavorite(session));
        favoriteButton.setOnCheckedChangeListener(this);
        apply(headers, TEXT_COLOR_SETTER, session.getBrandColor().getPrimary());

        setupFeedbackButton();

        setUpSessionDetailList();
        setUpSpeakerList();
    }

    @Override protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onSpeakerClick(Speaker speaker) {
        final String twitterUrl = getString(R.string.twitter_url, speaker.getTwitter());
        Intents.launchUrl(this, twitterUrl);
    }

    @Override public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        if (isChecked) {
            preferences.favorite(session);
        } else {
            preferences.unfavorite(session);
        }
    }

    @OnClick(R.id.submit_feedback) void onSubmitFeedbackClick() {
        final FeedbackFragment fragment = FeedbackFragment.newInstance(session);
        fragment.show(getSupportFragmentManager(), FeedbackFragment.TAG);
    }

    @Subscribe public void onSuccessfulFeedbackSubmitted(SuccessfulFeedbackSubmission event) {
        setupFeedbackButton();
    }

    private void setUpActionBar() {
        setSupportActionBar(getToolbar());
        getToolbar().setBackgroundColor(session.getBrandColor().getPrimary());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(session.getBrandColor().getSecondary());
    }

    private void setupFeedbackButton() {
        final boolean hasSubmittedFeedback = preferences.hasSubmittedFeedback(session);
        submitFeedback.setText(hasSubmittedFeedback ?
                R.string.feedback_submitted :
                R.string.submit_feedback);
        submitFeedback.setEnabled(!hasSubmittedFeedback);
    }

    private void setUpSessionDetailList() {
        final List<SessionDetail> sessionDetails = SessionDetails.builder()
                .add(R.drawable.ic_action_schedule, toDateString(session.getBeginning()))
                .add(R.drawable.ic_maps_place, session.getRoom().getName())
                .add(R.drawable.ic_action_description, fromHtml(session.getDescription()))
                .toList();

        final SessionDetailAdapter sessionDetailAdapter = new SessionDetailAdapter(sessionDetails);
        sessionDetailRecyclerView.setAdapter(sessionDetailAdapter);
        sessionDetailRecyclerView.setLayoutManager(new NestedLinearLayoutManager(this));
        scrollView.post(new Runnable() {
            @Override public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    private void setUpSpeakerList() {
        final List<Speaker> speakers = session.getSpeakers();
        speakersHeader.setText(getResources().getQuantityString(R.plurals.speakers, speakers.size()));
        speakerAdapter.setData(speakers);
        speakerAdapter.setOnSpeakerClickListener(this);
        speakerRecyclerView.setAdapter(speakerAdapter);
        speakerRecyclerView.setLayoutManager(new NestedLinearLayoutManager(this));
    }
}
