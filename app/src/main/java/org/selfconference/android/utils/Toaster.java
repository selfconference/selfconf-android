package org.selfconference.android.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import org.selfconference.android.App;

import static android.widget.Toast.LENGTH_SHORT;

public final class Toaster {

    public static void toast(@StringRes int stringResId) {
        Toast.makeText(App.getInstance(), stringResId, LENGTH_SHORT).show();
    }

    private Toaster() {
        throw new AssertionError("No instances.");
    }
}
