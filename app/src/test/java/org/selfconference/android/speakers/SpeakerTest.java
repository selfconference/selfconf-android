package org.selfconference.android.speakers;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.session.Session;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class SpeakerTest {

    @Test
    public void speakerParcelsWithoutErrors() throws Exception {
        final Speaker speaker = Speaker.builder()
                .bio("bio")
                .photo("http://test.com/img.png")
                .id(10)
                .name("Dave")
                .sessions(asList(
                        Session.builder().id(10).build(),
                        Session.builder().id(25).build()))
                .twitter("dave")
                .build();

        final Parcel parcel = Parcel.obtain();
        speaker.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final Speaker parceledSpeaker = Speaker.CREATOR.createFromParcel(parcel);

        assertThat(speaker).isEqualTo(parceledSpeaker);
    }
}