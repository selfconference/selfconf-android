package org.selfconference.android;

import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends ActionBarActivity {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Toolbar toolbar;

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    protected Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) checkNotNull(findViewById(R.id.toolbar));
        }
        return toolbar;
    }

    protected int getColor(@ColorRes int colorResId) {
        return getResources().getColor(colorResId);
    }

    protected void setStatusBarColor(int color) {
        if (SDK_INT >= LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
