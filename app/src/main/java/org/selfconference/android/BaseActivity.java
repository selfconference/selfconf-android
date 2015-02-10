package org.selfconference.android;

import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends ActionBarActivity {

    private Toolbar toolbar;

    protected Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) checkNotNull(findViewById(R.id.toolbar));
        }
        return toolbar;
    }


    protected int getColor(@ColorRes int colorResId) {
        return getResources().getColor(colorResId);
    }
}
