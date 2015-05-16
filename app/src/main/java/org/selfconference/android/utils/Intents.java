package org.selfconference.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public final class Intents {

    public static void launchUrl(Context context, String url) {
        final Intent intent = new Intent()
                .setAction(ACTION_VIEW)
                .setData(Uri.parse(url))
                .addFlags(FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private Intents() {}
}
