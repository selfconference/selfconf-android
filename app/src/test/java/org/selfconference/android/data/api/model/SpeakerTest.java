package org.selfconference.android.data.api.model;

import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.support.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.support.Parceler.testParceling;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public final class SpeakerTest {

  @Test @Ignore("Unable to handle Room Parcelable in Robolectric tests, but works on device")
  public void speakerParcelsWithoutErrors() throws Exception {
    Speaker speaker = Speaker.builder()
        .bio("bio")
        .photo("http://test.com/img.png")
        .id(10)
        .name("Dave")
        .sessions(ImmutableList.of(10))
        .twitter("dave")
        .build();

    Container<Speaker> speakerContainer = testParceling(speaker, AutoValue_Speaker.CREATOR);

    assertThat(speakerContainer.original).isEqualTo(speakerContainer.parceled);
  }
}
