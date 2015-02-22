package org.selfconference.android.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.App;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.api.Speaker;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.utils.NestedLinearLayoutManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.app.AppObservable;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.BrandColors.getPrimaryColorForPosition;
import static org.selfconference.android.utils.BrandColors.getSecondaryColorForPosition;
import static org.selfconference.android.utils.VersionHelper.setDrawableTint;
import static rx.android.app.AppObservable.bindActivity;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class SessionDetailsActivity extends BaseActivity implements SpeakerAdapter.OnSpeakerClickListener {
    private static final String EXTRA_SESSION = "org.selfconference.android.schedule.SESSION";

    @InjectView(R.id.long_title)
    TextView sessionTitle;

    @InjectView(R.id.session_description)
    TextView sessionDescription;

    @InjectView(R.id.speakers_header)
    TextView speakersHeader;

    @InjectView(R.id.favorite_button)
    ImageView favoriteButton;

    @InjectView(R.id.speaker_recycler_view)
    RecyclerView speakerRecyclerView;

    @Inject
    SelfConferenceApi api;

    @Inject
    SavedSessionPreferences preferences;

    private final SpeakerAdapter speakerAdapter = new SpeakerAdapter(true);

    private Session session;
    private Drawable favoriteDrawable;
    private Drawable unfavoriteDrawable;
    private int primaryColor;
    private int primaryDarkColor;

    public static Intent newIntent(final Context context, final Session session) {
        return new Intent(context, SessionDetailsActivity.class).putExtra(EXTRA_SESSION, session);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        App.getInstance().inject(this);
        ButterKnife.inject(this);

        session = (Session) checkNotNull(getIntent().getParcelableExtra(EXTRA_SESSION));

        setDetailColors();
        setUpActionBar();

        sessionTitle.setText(session.getTitle());
        sessionDescription.setText(Html.fromHtml(session.getDescription()));
        favoriteButton.setImageDrawable(preferences.isFavorite(session) ? getFavoriteDrawable() : getUnfavoriteDrawable());
        speakersHeader.setTextColor(primaryColor);

        speakerAdapter.setOnSpeakerClickListener(this);

        speakerRecyclerView.setAdapter(speakerAdapter);
        speakerRecyclerView.setLayoutManager(new NestedLinearLayoutManager(this));

        final Observable<List<Speaker>> speakersObservable = api.getSpeakersForSession(session);
        addSubscription(
                bindActivity(this, speakersObservable).subscribe(speakersSubscriber)
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSpeakerClick(Speaker speaker) {
        final String twitterUri = String.format("https://twitter.com/%s", speaker.getTwitter());
        final Intent twitterIntent = new Intent()
                .setAction(ACTION_VIEW)
                .setData(Uri.parse(twitterUri))
                .addFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(twitterIntent);
    }

    @OnClick(R.id.favorite_button)
    void onFavoriteButtonClicked() {
        if (preferences.isFavorite(session)) {
            preferences.removeFavorite(session);
            favoriteButton.setImageDrawable(getUnfavoriteDrawable());
        } else {
            preferences.saveFavorite(session);
            favoriteButton.setImageDrawable(getFavoriteDrawable());
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(getToolbar());
        getToolbar().setBackgroundColor(primaryColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(primaryDarkColor);
    }

    private void setDetailColors() {
        final int sessionId = session.getId();
        primaryColor = getPrimaryColorForPosition(this, sessionId);
        primaryDarkColor = getSecondaryColorForPosition(this, sessionId);
    }

    private Drawable getUnfavoriteDrawable() {
        if (unfavoriteDrawable == null) {
            unfavoriteDrawable = getResources().getDrawable(R.drawable.ic_star_outline_grey600_24dp);
            setDrawableTint(unfavoriteDrawable, primaryColor);
        }
        return unfavoriteDrawable;
    }

    private Drawable getFavoriteDrawable() {
        if (favoriteDrawable == null) {
            favoriteDrawable = getResources().getDrawable(R.drawable.ic_star_grey600_24dp);
            setDrawableTint(favoriteDrawable, primaryColor);
        }
        return favoriteDrawable;
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
            speakersHeader.setText(getResources().getQuantityString(R.plurals.speakers, speakers.size()));
            speakerAdapter.setSpeakers(speakers);
        }
    };
}
