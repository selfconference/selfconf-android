package org.selfconference.data.api.model;

import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.selfconference.support.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.support.Parceler.testParceling;

@RunWith(RobolectricTestRunner.class)
public final class SpeakerTest {

  @Test @Ignore("Unable to handle Room Parcelable in Robolectric tests, but works on device")
  public void speakerParcelsWithoutErrors() throws Exception {
    Speaker speaker = Speaker.builder()
        .bio("bio")
        .headshot("http://test.com/img.png")
        .id(10)
        .name("Dave")
        .sessions(ImmutableList.of())
        .twitter("dave")
        .build();

    Container<Speaker> speakerContainer = testParceling(speaker, AutoValue_Speaker.CREATOR);

    assertThat(speakerContainer.original).isEqualTo(speakerContainer.parceled);
  }
}
