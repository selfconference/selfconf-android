package org.selfconference.android.ui.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.squareup.picasso.Picasso;
import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.data.IntentFactory;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.BaseActivity;
import org.selfconference.android.ui.ViewContainer;
import org.selfconference.android.ui.misc.Themes;
import org.selfconference.android.ui.session.FeedbackFragment.OnFeedbackCreatedListener;
import org.selfconference.android.ui.speaker.SpeakerAdapter;
import org.selfconference.android.ui.view.FloatingActionButton;
import org.selfconference.android.ui.viewmodel.SessionDetail;
import org.selfconference.android.ui.viewmodel.SessionDetails;
import org.selfconference.android.util.Instants;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava2.Result;

import static android.view.View.GONE;
import static com.google.android.material.snackbar.Snackbar.LENGTH_SHORT;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SessionDetailActivity extends BaseActivity implements OnFeedbackCreatedListener {
  private static final String EXTRA_SESSION = "org.selfconference.android.ui.session.SESSION";

  @BindView(R.id.long_title) TextView sessionTitle;
  @BindView(R.id.scroll_view) ScrollView scrollView;
  @BindView(R.id.speakers_header) TextView speakersHeader;
  @BindView(R.id.favorite_button) FloatingActionButton favoriteButton;
  @BindView(R.id.session_detail_recycler_view) RecyclerView sessionDetailRecyclerView;
  @BindView(R.id.speaker_recycler_view) RecyclerView speakerRecyclerView;
  @BindView(R.id.submit_feedback) TextView submitFeedback;

  @Inject SessionPreferences preferences;
  @Inject Picasso picasso;
  @Inject IntentFactory intentFactory;
  @Inject ViewContainer viewContainer;
  @Inject RestClient restClient;

  private final PublishSubject<Feedback> feedbackSubject = PublishSubject.create();
  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private Session session;

  public static Intent newIntent(Context context, Session session) {
    return new Intent(context, SessionDetailActivity.class).putExtra(EXTRA_SESSION, session);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App.context().getApplicationComponent().inject(this);

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
    favoriteButton.setOnCheckedChangeListener((fabView, isChecked) -> {
      if (isChecked) {
        preferences.favorite(session);
      } else {
        preferences.unfavorite(session);
      }
    });
    favoriteButton.setOnClickListener(view -> showSnackbar());

    setupFeedbackButton();

    setUpSessionDetailList();
    setUpSpeakerList();

    Observable<Result<ResponseBody>> result = feedbackSubject
        .flatMap(feedback -> restClient.submitFeedback(session.id(), feedback)
            .subscribeOn(Schedulers.io()))
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindToLifecycle())
        .share();

    Disposable feedbackSuccess = result.filter(Results.isSuccessful()).subscribe(response -> {
      preferences.submitFeedback(session);
      setupFeedbackButton();
    });
    compositeDisposable.add(feedbackSuccess);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
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

  @Override public void onFeedbackCreated(Session session, Feedback feedback) {
    submitFeedback.setEnabled(false);
    submitFeedback.setText(R.string.message_feedback_submit);

    // Submit feedback.
    feedbackSubject.onNext(feedback);
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
        .add(R.drawable.ic_action_schedule, Instants.dayTimeString(slot.time()))
        .add(R.drawable.ic_action_description, nullSafeDescription(session))
        .toList();

    SessionDetailAdapter sessionDetailAdapter = new SessionDetailAdapter(sessionDetails);
    sessionDetailRecyclerView.setAdapter(sessionDetailAdapter);
    sessionDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    scrollView.post(() -> scrollView.scrollTo(0, 0));
  }

  private static String nullSafeDescription(Session session) {
    Optional<String> optionalDescription = Optional.fromNullable(session.description());
    if (optionalDescription.isPresent()) {
      return Html.fromHtml(optionalDescription.get()).toString();
    }
    return "No description";
  }

  private void setUpSpeakerList() {
    List<Speaker> speakers = Optional.fromNullable(session.speakers()).or(ImmutableList.of());
    if (speakers.isEmpty()) {
      speakersHeader.setVisibility(GONE);
    } else {
      speakersHeader.setText(getResources().getQuantityString(R.plurals.speakers, speakers.size()));
      SpeakerAdapter speakerAdapter = new SpeakerAdapter(picasso);
      speakerAdapter.setSpeakers(speakers);
      speakerAdapter.setOnSpeakerClickListener(speaker -> {
        String twitterUrl = getString(R.string.twitter_url, speaker.twitter());
        startActivity(intentFactory.createUrlIntent(twitterUrl));
      });
      speakerRecyclerView.setAdapter(speakerAdapter);
      speakerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
  }

  private void showSnackbar() {
    boolean isChecked = favoriteButton.isChecked();
    @StringRes int messageResId =
        isChecked ? R.string.message_schedule_add : R.string.message_schedule_remove;
    Snackbar snackbar = Snackbar.make(favoriteButton, messageResId, LENGTH_SHORT);
    snackbar.setAction(R.string.undo, v -> {
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
