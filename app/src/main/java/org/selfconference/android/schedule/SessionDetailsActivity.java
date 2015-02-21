package org.selfconference.android.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.App;
import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.api.Session;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.BrandColors.getPrimaryColorForPosition;
import static org.selfconference.android.utils.BrandColors.getSecondaryColorForPosition;
import static org.selfconference.android.utils.VersionHelper.setDrawableTint;

public class SessionDetailsActivity extends BaseActivity {
    private static final String EXTRA_SESSION = "org.selfconference.android.schedule.SESSION";

    @InjectView(R.id.long_title)
    TextView sessionTitle;

    @InjectView(R.id.session_description)
    TextView sessionDescription;

    @InjectView(R.id.favorite_button)
    ImageView favoriteButton;

    @Inject
    SavedSessionPreferences preferences;

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

        setSession();
        setDetailColors();

        setSupportActionBar(getToolbar());
        getToolbar().setBackgroundColor(primaryColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(primaryDarkColor);

        sessionTitle.setText(session.getTitle());
        sessionDescription.setText(Html.fromHtml(session.getDescription()));
        favoriteButton.setImageDrawable(preferences.isFavorite(session) ? getFavoriteDrawable() : getUnfavoriteDrawable());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void setSession() {
        final Intent intent = checkNotNull(getIntent());
        session = (Session) checkNotNull(intent.getParcelableExtra(EXTRA_SESSION));
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
}
