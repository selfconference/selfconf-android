package org.selfconference.android.utils;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class DateStringerTest {

    @Test
    public void testToday() {
        final DateTime nowPlusFiveMinutes = DateTime.now().plusMinutes(5);

        final String dateString =
                DateStringer.toDateString(nowPlusFiveMinutes);

        assertThat(dateString).startsWith("Today at");
    }

    @Test
    public void testTomorrow() {
        final DateTime nowPlusFiveMinutes = DateTime.now().plusDays(1).plusMinutes(5);

        final String dateString =
                DateStringer.toDateString(nowPlusFiveMinutes);

        assertThat(dateString).startsWith("Tomorrow at");
    }
}