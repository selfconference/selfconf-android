package org.selfconference.android.utils;

import android.os.Handler;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import timber.log.Timber;

import static android.os.Looper.getMainLooper;
import static android.os.Looper.myLooper;

/**
 * This message bus allows you to post a message from any thread and it will get handled and then
 * posted to the main thread for you.
 * <p/>
 * via <a href="https://github.com/AndroidBootstrap/android-bootstrap">Android Bootstrap</a>
 */
public final class PostFromAnyThreadBus extends Bus {

    public PostFromAnyThreadBus() {
        super(ThreadEnforcer.MAIN);
    }

    @Override public void post(final Object event) {
        if (myLooper() != getMainLooper()) {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override public void run() {
                    PostFromAnyThreadBus.super.post(event);
                }
            });
        } else {
            super.post(event);
        }
    }

    @Override public void unregister(final Object object) {
        try {
            super.unregister(object);
        } catch (IllegalArgumentException e) {
            Timber.e(e, e.getMessage());
        }
    }
}
