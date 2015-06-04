package org.selfconference.android.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.selfconference.android.App;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.brand.BrandColor;
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
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.text.Html.fromHtml;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.DateStringer.toDateString;

public class SessionDetailsActivity extends BaseActivity implements OnSpeakerClickListener, OnCheckedChangeListener {
    private static final String EXTRA_SESSION = "org.selfconference.android.session.SESSION";

    @InjectView(R.id.long_title) TextView sessionTitle;
    @InjectView(R.id.scroll_view) ScrollView scrollView;
    @InjectView(R.id.speakers_header) TextView speakersHeader;
    @InjectView(R.id.favorite_button) FloatingActionButton favoriteButton;
    @InjectView(R.id.session_detail_recycler_view) RecyclerView sessionDetailRecyclerView;
    @InjectView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
    @InjectView(R.id.submit_feedback) TextView submitFeedback;

    @Inject SessionPreferences preferences;
    @Inject Bus bus;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(true);

    private Session session;

    public static Intent newIntent(final Context context, final Session session) {
        return new Intent(context, SessionDetailsActivity.class).putExtra(EXTRA_SESSION, session);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = checkNotNull((Session) getIntent().getParcelableExtra(EXTRA_SESSION));

        setTheme(BrandColor.forId(session.getId()));
        setStatusBarColor(resolveStatusBarColor());

        setContentView(R.layout.activity_session_details);
        App.getInstance().inject(this);
        ButterKnife.inject(this);

        setUpActionBar();

        sessionTitle.setText(session.getTitle());
        favoriteButton.setChecked(preferences.isFavorite(session));
        favoriteButton.setOnCheckedChangeListener(this);
        favoriteButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                showSnackbar();
            }
        });

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void showSnackbar() {
        final boolean isChecked = favoriteButton.isChecked();
        final String message = isChecked ? "Session favorited" : "Session unfavorited";
        final Snackbar snackbar = Snackbar.make(favoriteButton, message, LENGTH_SHORT);
        snackbar.setAction("Undo", new OnClickListener() {
            @Override public void onClick(View v) {
                favoriteButton.setChecked(!isChecked);
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private int resolveStatusBarColor() {
        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }
}
