package org.selfconference.android.api;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class SpeakerTest {

    @Test
    public void speakerParcelsWithoutErrors() throws Exception {
        final Speaker speaker = new Speaker.Builder()
                .bio("bio")
                .headshot("http://test.com/img.png")
                .id(10)
                .name("Dave")
                .sessions(asList(1, 2, 4))
                .twitter("dave")
                .build();

        final Parcel parcel = Parcel.obtain();
        speaker.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        final Speaker parceledSpeaker = Speaker.CREATOR.createFromParcel(parcel);

        assertThat(speaker).isEqualTo(parceledSpeaker);
    }
}