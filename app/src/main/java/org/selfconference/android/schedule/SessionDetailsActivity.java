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
import org.selfconference.android.utils.StatusBarHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionDetailsActivity extends BaseActivity {
    private static final String EXTRA_SESSION = "org.selfconference.android.schedule.SESSION";

    @InjectView(R.id.session_title)
    TextView sessionTitle;

    @InjectView(R.id.session_description)
    TextView sessionDescription;

    @InjectView(R.id.favorite_button)
    ImageView favoriteButton;

    @Inject
    SavedSessionPreferences preferences;

    private Session session;

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

        final Intent intent = checkNotNull(getIntent());
        session = (Session) checkNotNull(intent.getParcelableExtra(EXTRA_SESSION));

        setSupportActionBar(getToolbar());
        StatusBarHelper.setStatusBarColor(getWindow(), R.color.primary_dark);

        sessionTitle.setText(session.getTitle());
        sessionDescription.setText(Html.fromHtml(session.getDescription()));
        favoriteButton.setImageResource(R.drawable.ic_star_outline_grey600_24dp);

        favoriteButton.setImageDrawable(preferences.isFavorite(session) ? favoriteDrawable() : unfavoriteDrawable());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Drawable unfavoriteDrawable() {
        final Drawable unfavoritedDrawable = getResources().getDrawable(R.drawable.ic_star_outline_grey600_24dp);
        unfavoritedDrawable.setTint(getColor(R.color.purple));
        return unfavoritedDrawable;
    }

    private Drawable favoriteDrawable() {
        final Drawable favoritedDrawable = getResources().getDrawable(R.drawable.ic_star_grey600_24dp);
        favoritedDrawable.setTint(getColor(R.color.purple));
        return favoritedDrawable;
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
            favoriteButton.setImageDrawable(unfavoriteDrawable());
        } else {
            preferences.saveFavorite(session);
            favoriteButton.setImageDrawable(favoriteDrawable());
        }
    }
}
