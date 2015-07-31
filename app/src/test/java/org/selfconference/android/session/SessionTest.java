package org.selfconference.android.session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;
import org.selfconference.android.speakers.Speaker;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTime.now;
import static org.selfconference.android.Parceler.testParceling;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml") public class SessionTest {

  @Test public void sessionParcelsWithoutError() throws Exception {
    final Session session = Session.builder()
        .id(4)
        .beginning(now())
        .description("description")
        .room(Room.builder().name("101").build())
        .speakers(asList(Speaker.builder().id(3).build(), Speaker.builder().id(5).build()))
        .title("title")
        .build();

    final Container<Session> sessionContainer = testParceling(session, Session.CREATOR);

    assertThat(sessionContainer.original).isEqualTo(sessionContainer.parceled);
  }
}