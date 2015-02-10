package org.selfconference.android.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.selfconference.android.BaseActivity;
import org.selfconference.android.R;
import org.selfconference.android.utils.StatusBarHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.google.common.base.Preconditions.checkNotNull;

public class SessionActivity extends BaseActivity {
    public static final String TAG = SessionActivity.class.getName();
    private static final String EXTRA_SESSION = TAG + "." + "session";

    public static Intent newIntent(final Context context, final Session session) {
        return new Intent(context, SessionActivity.class).putExtra(EXTRA_SESSION, session);
    }

    @InjectView(R.id.session_title)
    TextView sessionTitle;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        ButterKnife.inject(this);

        final Intent intent = checkNotNull(getIntent());
        final Session session = (Session) checkNotNull(intent.getParcelableExtra(EXTRA_SESSION));

        setSupportActionBar(getToolbar());
        StatusBarHelper.setStatusBarColor(getWindow(), R.color.primary_dark);

        sessionTitle.setText(session.getTitle());
        getSupportActionBar().setTitle(session.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
