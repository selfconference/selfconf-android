package org.selfconference.android.utils;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowIntent;
import org.selfconference.android.CustomTestRunner;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(CustomTestRunner.class)
public class IntentsTest {

    @Test
    public void testLaunchUrl() {
        Intents.launchUrl(Robolectric.application, "https://google.com");

        final Intent intent = Robolectric.getShadowApplication().peekNextStartedActivity();
        final ShadowIntent shadowIntent = shadowOf(intent);

        assertThat(shadowIntent.getAction())
                .isEqualTo(ACTION_VIEW);

        assertThat(shadowIntent.getFlags())
                .isEqualTo(FLAG_ACTIVITY_CLEAR_TASK);

        assertThat(shadowIntent.getDataString())
                .isEqualTo("https://google.com");
    }
}