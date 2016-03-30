package org.selfconference.android.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.App;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.brand.BrandColor;
import org.selfconference.android.feedback.FeedbackFragment;
import org.selfconference.android.feedback.SuccessfulFeedbackSubmission;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.utils.Intents;
import org.selfconference.android.views.FloatingActionButton;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.text.Html.fromHtml;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.DateStringer.toDateString;

public final class SessionDetailsActivity extends BaseActivity {
  private static final String EXTRA_SESSION = "org.selfconference.android.session.SESSION";

  @Bind(R.id.long_title) TextView sessionTitle;
  @Bind(R.id.scroll_view) ScrollView scrollView;
  @Bind(R.id.speakers_header) TextView speakersHeader;
  @Bind(R.id.favorite_button) FloatingActionButton favoriteButton;
  @Bind(R.id.session_detail_recycler_view) RecyclerView sessionDetailRecyclerView;
  @Bind(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
  @Bind(R.id.submit_feedback) TextView submitFeedback;

  @Inject SessionPreferences preferences;
  @Inject Bus bus;

  private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(true);

  private Session session;

  public static Intent newIntent(Context context, Session session) {
    return new Intent(context, SessionDetailsActivity.class).putExtra(EXTRA_SESSION, session);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    session = checkNotNull((Session) getIntent().getParcelableExtra(EXTRA_SESSION));

    setTheme(BrandColor.forId(session.id()));
    setStatusBarColor(resolveStatusBarColor());

    setContentView(R.layout.activity_session_details);
    App.getInstance().inject(this);
    ButterKnife.bind(this);

    setUpActionBar();

    sessionTitle.setText(session.title());
    favoriteButton.setChecked(preferences.isFavorite(session));
    favoriteButton.setOnCheckedChangeListener((fabView, isChecked) -> {
      if (isChecked) {
        preferences.favorite(session);
      } else {
        preferences.unfavorite(session);
      }
    });
    favoriteButton.setOnClickListener(this::showSnackbar);

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

  @OnClick(R.id.submit_feedback) void onSubmitFeedbackClick() {
    FeedbackFragment fragment = FeedbackFragment.newInstance(session);
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
    submitFeedback.setText(
        hasSubmittedFeedback ? R.string.feedback_submitted : R.string.submit_feedback);
    submitFeedback.setEnabled(!hasSubmittedFeedback);
  }

  private void setUpSessionDetailList() {
    SessionDetails.Builder builder = SessionDetails.builder();
    builder.add(R.drawable.ic_maps_place, session.room().name());
    if (session.beginning() != null) {
      builder.add(R.drawable.ic_action_schedule, toDateString(session.beginning()));
    }
    List<SessionDetail> sessionDetails =
        builder.add(R.drawable.ic_action_description, fromHtml(session.description())).toList();

    SessionDetailAdapter sessionDetailAdapter = new SessionDetailAdapter(sessionDetails);
    sessionDetailRecyclerView.setAdapter(sessionDetailAdapter);
    sessionDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    scrollView.post(() -> scrollView.scrollTo(0, 0));
  }

  private void setUpSpeakerList() {
    List<Speaker> speakers = session.speakers();
    speakersHeader.setText(getResources().getQuantityString(R.plurals.speakers, speakers.size()));
    speakerAdapter.setData(speakers);
    speakerAdapter.setOnSpeakerClickListener(speaker -> {
      String twitterUrl = getString(R.string.twitter_url, speaker.twitter());
      Intents.launchUrl(this, twitterUrl);
    });
    speakerRecyclerView.setAdapter(speakerAdapter);
    speakerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void showSnackbar(View view) {
    boolean isChecked = favoriteButton.isChecked();
    String message = isChecked ? "Session favorited" : "Session unfavorited";
    Snackbar snackbar = Snackbar.make(favoriteButton, message, LENGTH_SHORT);
    snackbar.setAction("Undo", v -> {
      favoriteButton.setChecked(!isChecked);
      snackbar.dismiss();
    });
    snackbar.show();
  }

  private int resolveStatusBarColor() {
    TypedValue typedValue = new TypedValue();
    getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
    return typedValue.data;
  }
}
