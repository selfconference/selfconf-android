package org.selfconference.android.ui.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.selfconference.android.R;
import org.selfconference.android.data.IntentFactory;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.event.SubmitFeedbackAddEvent;
import org.selfconference.android.data.event.SubmitFeedbackSuccessEvent;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseActivity;
import org.selfconference.android.ui.ViewContainer;
import org.selfconference.android.ui.misc.Themes;
import org.selfconference.android.ui.speaker.SpeakerAdapter;
import org.selfconference.android.ui.view.FloatingActionButton;
import org.selfconference.android.ui.viewmodel.SessionDetail;
import org.selfconference.android.ui.viewmodel.SessionDetails;
import org.selfconference.android.util.Instants;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.text.Html.fromHtml;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public final class SessionDetailActivity extends BaseActivity {
  private static final String EXTRA_SESSION = "org.selfconference.android.ui.session.SESSION";

  @BindView(R.id.long_title) TextView sessionTitle;
  @BindView(R.id.scroll_view) ScrollView scrollView;
  @BindView(R.id.speakers_header) TextView speakersHeader;
  @BindView(R.id.favorite_button) FloatingActionButton favoriteButton;
  @BindView(R.id.session_detail_recycler_view) RecyclerView sessionDetailRecyclerView;
  @BindView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
  @BindView(R.id.submit_feedback) TextView submitFeedback;

  @Inject SessionPreferences preferences;
  @Inject EventBus eventBus;
  @Inject Picasso picasso;
  @Inject IntentFactory intentFactory;
  @Inject ViewContainer viewContainer;

  private Session session;

  public static Intent newIntent(Context context, Session session) {
    return new Intent(context, SessionDetailActivity.class).putExtra(EXTRA_SESSION, session);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    session = getIntent().getParcelableExtra(EXTRA_SESSION);
    checkNotNull(session, "session == null");

    setTheme(Themes.forId(session.id()));
    setStatusBarColor(resolveStatusBarColor());

    ViewGroup container = viewContainer.forActivity(this);
    getLayoutInflater().inflate(R.layout.activity_session_details, container);
    ButterKnife.bind(this, container);

    setUpActionBar();

    sessionTitle.setText(session.name());
    favoriteButton.setChecked(preferences.isFavorite(session));
    favoriteButton.setOnCheckedChangeListener(new FloatingActionButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(FloatingActionButton fabView, boolean isChecked) {
        if (isChecked) {
          preferences.favorite(session);
        } else {
          preferences.unfavorite(session);
        }
      }
    });
    favoriteButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        showSnackbar();
      }
    });

    setupFeedbackButton();

    setUpSessionDetailList();
    setUpSpeakerList();
  }

  @Override protected void onResume() {
    super.onResume();
    eventBus.register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    eventBus.unregister(this);
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

  @Subscribe(threadMode = MAIN) public void onSubmitFeedbackAdded(SubmitFeedbackAddEvent event) {
    submitFeedback.setEnabled(false);
    submitFeedback.setText("Submitting feedback...");
  }

  @Subscribe(threadMode = MAIN)
  public void onSuccessfulFeedbackSubmitted(SubmitFeedbackSuccessEvent event) {
    setupFeedbackButton();
  }

  private void setUpActionBar() {
    setSupportActionBar(getToolbar());
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void setupFeedbackButton() {
    boolean hasSubmittedFeedback = preferences.hasSubmittedFeedback(session);
    submitFeedback.setText(
        hasSubmittedFeedback ? R.string.feedback_submitted : R.string.submit_feedback);
    submitFeedback.setEnabled(!hasSubmittedFeedback);
  }

  private void setUpSessionDetailList() {
    Room room = Optional.fromNullable(session.room()).or(Room.empty());
    Slot slot = Optional.fromNullable(session.slot()).or(Slot.empty());
    List<SessionDetail> sessionDetails = SessionDetails.builder()
        .add(R.drawable.ic_maps_place, room.name())
        .add(R.drawable.ic_action_schedule, Instants.shortTimeString(slot.time()))
        .add(R.drawable.ic_action_description, fromHtml(session.description()))
        .toList();

    SessionDetailAdapter sessionDetailAdapter = new SessionDetailAdapter(sessionDetails);
    sessionDetailRecyclerView.setAdapter(sessionDetailAdapter);
    sessionDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    scrollView.post(new Runnable() {
      @Override public void run() {
        scrollView.scrollTo(0, 0);
      }
    });
  }

  private void setUpSpeakerList() {
    List<Speaker> speakers = Optional.fromNullable(session.speakers()).or(ImmutableList.of());
    speakersHeader.setText(getResources().getQuantityString(R.plurals.speakers, speakers.size()));
    SpeakerAdapter speakerAdapter = new SpeakerAdapter(picasso, true);
    speakerAdapter.setData(speakers);
    speakerAdapter.setOnSpeakerClickListener(new SpeakerAdapter.OnSpeakerClickListener() {
      @Override public void onSpeakerClick(Speaker speaker) {
        String twitterUrl = getString(R.string.twitter_url, speaker.twitter());
        startActivity(intentFactory.createUrlIntent(twitterUrl));
      }
    });
    speakerRecyclerView.setAdapter(speakerAdapter);
    speakerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void showSnackbar() {
    final boolean isChecked = favoriteButton.isChecked();
    String message = isChecked ? "Session favorited" : "Session unfavorited";
    final Snackbar snackbar = Snackbar.make(favoriteButton, message, LENGTH_SHORT);
    snackbar.setAction("Undo", new View.OnClickListener() {
      @Override public void onClick(View v) {
        favoriteButton.setChecked(!isChecked);
        snackbar.dismiss();
      }
    });
    snackbar.show();
  }

  private int resolveStatusBarColor() {
    TypedValue typedValue = new TypedValue();
    getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
    return typedValue.data;
  }
}
