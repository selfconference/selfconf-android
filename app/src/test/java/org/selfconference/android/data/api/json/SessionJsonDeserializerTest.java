package org.selfconference.android.data.api.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.support.file.Sessions;
import org.threeten.bp.Instant;

import static org.selfconference.android.support.asserts.SessionAssert.assertThat;

public final class SessionJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder() //
        .registerTypeAdapter(Session.class, new SessionJsonDeserializer()) //
        .registerTypeAdapter(Room.class, new RoomJsonDeserializer()) //
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter()) //
        .create();
  }

  @Test public void deserializesSessionWithoutBeginningJsonProperly() {
    Session session = gson.fromJson(Sessions.withoutBeginning(), Session.class);

    assertThat(session)
        .hasId(57)
        .hasName("Reproducibility")
        .hasRoom(Room.create(7, "Salon A"))
        .hasBeginning(Instant.MIN)
        .hasDescription("To write code efficiently, we need to be able to rely on our tools. Editors always save files when we ask them to, version control systems restore old files when we ask them to, and so on. This is reproducibility: the tool reliably does the same thing when given the same inputs. Many tools lack this reliability, but there does seem to be a positive trend, which we examine in this talk. First, we compare the designs of Git, React, and Bundler, each of which relies on reproducibility and was a huge improvement over its predecessors. Then, we imagine what other benefits might come from continuing to focus on reproducibility in our tools.")
        .hasSpeakersSize(1)
        .isKeynote();
  }

  @Test public void deserializesMissingRoomAsNullObject() {
    Session session = gson.fromJson(Sessions.withoutRoom(), Session.class);

    assertThat(session).hasRoom(Room.nullRoom());
  }

  @Test public void deserializesNullKeynoteAsFalse() {
    Session session = gson.fromJson(Sessions.withNullKeynote(), Session.class);

    assertThat(session).isNotKeynote();
  }

  @Test public void deserializesMissingKeynoteAsFalse() {
    Session session = gson.fromJson(Sessions.withoutKeynote(), Session.class);

    assertThat(session).isNotKeynote();
  }
}
