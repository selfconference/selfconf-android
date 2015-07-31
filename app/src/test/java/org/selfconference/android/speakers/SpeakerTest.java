package org.selfconference.android.speakers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;
import org.selfconference.android.session.Session;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.Parceler.testParceling;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml") public class SpeakerTest {

  @Test public void speakerParcelsWithoutErrors() throws Exception {
    final Speaker speaker = Speaker.builder()
        .bio("bio")
        .photo("http://test.com/img.png")
        .id(10)
        .name("Dave")
        .sessions(asList(Session.builder().id(10).build(), Session.builder().id(25).build()))
        .twitter("dave")
        .build();

    final Container<Speaker> speakerContainer = testParceling(speaker, Speaker.CREATOR);

    assertThat(speakerContainer.original).isEqualTo(speakerContainer.parceled);
  }
}