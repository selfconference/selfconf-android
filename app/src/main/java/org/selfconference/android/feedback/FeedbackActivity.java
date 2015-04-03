package org.selfconference.android.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.api.Session;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.selfconference.android.utils.BrandColors.getPrimaryColorForPosition;
import static org.selfconference.android.utils.BrandColors.getSecondaryColorForPosition;

public class FeedbackActivity extends BaseActivity {
    private static final String EXTRA_SESSION = "session";

    private Session session;
    private int primaryColor;
    private int primaryDarkColor;

    public static Intent newIntent(Context context, Session session) {
        return new Intent(context, FeedbackActivity.class)
                .putExtra(EXTRA_SESSION, session);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        session = checkNotNull((Session) getIntent().getParcelableExtra(EXTRA_SESSION));

        setDetailColors();
        setupActionBar();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        setSupportActionBar(getToolbar());
        getToolbar().setBackgroundColor(primaryColor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(primaryDarkColor);
    }

    private void setDetailColors() {
        final int sessionId = session.getId();
        primaryColor = getPrimaryColorForPosition(sessionId);
        primaryDarkColor = getSecondaryColorForPosition(sessionId);
    }
}
