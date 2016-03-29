package org.selfconference.android.session;

import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;
import org.selfconference.android.speakers.Speaker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTime.now;
import static org.selfconference.android.Parceler.testParceling;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public final class SessionTest {

  @Test @Ignore("Unable to handle Room Parcelable in Robolectric tests, but works on device")
  public void sessionParcelsWithoutError() throws Exception {
    Session session = Session.builder()
        .id(4)
        .beginning(now())
        .description("description")
        .room(Room.emptyRoom())
        .speakers(ImmutableList.of(Speaker.builder() //
            .id(3)
            .name("Name")
            .twitter("name")
            .bio("bio")
            .photo("http://example.com/photo.jpg")
            .build()))
        .title("title")
        .keynote(false)
        .build();

    Container<Session> sessionContainer = testParceling(session, AutoValue_Session.CREATOR);

    assertThat(sessionContainer.original).isEqualTo(sessionContainer.parceled);
  }
}
