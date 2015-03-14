package org.selfconference.android.api;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTime.now;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class SessionTest {

    @Test
    public void sessionParcelsWithoutError() throws Exception {
        final Session session = new Session.Builder()
                .id(4)
                .beginning(now())
                .description("description")
                .room("101")
                .speakers(asList(3, 4, 9))
                .title("title")
                .build();

        final Parcel parcel = Parcel.obtain();
        session.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final Session parceledSession = Session.CREATOR.createFromParcel(parcel);

        assertThat(session).isEqualTo(parceledSession);
    }
}