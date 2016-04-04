package org.selfconference.android.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.data.api.NullDateTime;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerTypeAdapter;

import static org.selfconference.android.session.SessionAssert.assertThat;

public final class SessionJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder() //
        .registerTypeAdapter(Session.class, new SessionJsonDeserializer()) //
        .registerTypeAdapter(Room.class, new RoomJsonDeserializer()) //
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter()) //
        .create();
  }

  @Test public void deserializesCompleteSessionJsonProperly() {
    Session session = gson.fromJson(completeSessionJson(), Session.class);

    assertThat(session)
        .hasId(57)
        .hasName("Reproducibility")
        .hasRoom(Room.create(7, "Salon A"))
        .hasBeginning(NullDateTime.create())
        .hasDescription("To write code efficiently, we need to be able to rely on our tools. Editors always save files when we ask them to, version control systems restore old files when we ask them to, and so on. This is reproducibility: the tool reliably does the same thing when given the same inputs. Many tools lack this reliability, but there does seem to be a positive trend, which we examine in this talk. First, we compare the designs of Git, React, and Bundler, each of which relies on reproducibility and was a huge improvement over its predecessors. Then, we imagine what other benefits might come from continuing to focus on reproducibility in our tools.")
        .hasSpeakersSize(1)
        .isKeynote(true);
  }

  @Test public void deserializesMissingRoomAsEmptyRoom() {
    Session session = gson.fromJson(sessionWithoutRoomJson(), Session.class);

    assertThat(session).hasRoom(Room.nullRoom());
  }

  private static String completeSessionJson() {
    return "{\n"
        + "  \"abstract\": \"To write code efficiently, we need to be able to rely on our tools. Editors always save files when we ask them to, version control systems restore old files when we ask them to, and so on. This is reproducibility: the tool reliably does the same thing when given the same inputs. Many tools lack this reliability, but there does seem to be a positive trend, which we examine in this talk. First, we compare the designs of Git, React, and Bundler, each of which relies on reproducibility and was a huge improvement over its predecessors. Then, we imagine what other benefits might come from continuing to focus on reproducibility in our tools.\",\n"
        + "  \"created_at\": \"2016-02-02T20:33:17.880-05:00\",\n"
        + "  \"event_id\": 2,\n"
        + "  \"id\": 57,\n"
        + "  \"keynote\": true,\n"
        + "  \"name\": \"Reproducibility\",\n"
        + "  \"room\": {\n"
        + "    \"created_at\": \"2015-07-23T13:24:47.094-05:00\",\n"
        + "    \"event_id\": 2,\n"
        + "    \"id\": 7,\n"
        + "    \"name\": \"Salon A\",\n"
        + "    \"order\": 0,\n"
        + "    \"updated_at\": \"2016-03-03T00:56:47.320-05:00\",\n"
        + "    \"venue_id\": 1\n"
        + "  },\n"
        + "  \"room_id\": 7,\n"
        + "  \"slot_id\": 16,\n"
        + "  \"speakers\": [\n"
        + "    {\n"
        + "      \"bio\": \"Gary Bernhardt is a creator and destroyer of software compelled to understand both sides of heated software debates: Vim and Emacs; Python and Ruby; Git and Mercurial. He runs Destroy All Software, which publishes advanced screencasts for serious developers covering Unix, OO design, TDD, and dynamic languages.\",\n"
        + "      \"created_at\": \"2016-02-02T20:32:20.498-05:00\",\n"
        + "      \"event_id\": 2,\n"
        + "      \"id\": 62,\n"
        + "      \"name\": \"Gary Bernhardt\",\n"
        + "      \"photo\": \"https://s3.amazonaws.com/selfconf/speakers/gary-bernhardt.png\",\n"
        + "      \"twitter\": \"garybernhardt\",\n"
        + "      \"updated_at\": \"2016-02-02T21:00:26.659-05:00\"\n"
        + "    }\n"
        + "  ],\n"
        + "  \"updated_at\": \"2016-03-06T18:40:32.797-05:00\"\n"
        + "}";
  }

  private static String sessionWithoutRoomJson() {
    return "{\n"
        + "  \"abstract\": \"To write code efficiently, we need to be able to rely on our tools. Editors always save files when we ask them to, version control systems restore old files when we ask them to, and so on. This is reproducibility: the tool reliably does the same thing when given the same inputs. Many tools lack this reliability, but there does seem to be a positive trend, which we examine in this talk. First, we compare the designs of Git, React, and Bundler, each of which relies on reproducibility and was a huge improvement over its predecessors. Then, we imagine what other benefits might come from continuing to focus on reproducibility in our tools.\",\n"
        + "  \"created_at\": \"2016-02-02T20:33:17.880-05:00\",\n"
        + "  \"event_id\": 2,\n"
        + "  \"id\": 57,\n"
        + "  \"keynote\": true,\n"
        + "  \"name\": \"Reproducibility\",\n"
        + "  \"room_id\": 7,\n"
        + "  \"slot_id\": 16,\n"
        + "  \"speakers\": [\n"
        + "    {\n"
        + "      \"bio\": \"Gary Bernhardt is a creator and destroyer of software compelled to understand both sides of heated software debates: Vim and Emacs; Python and Ruby; Git and Mercurial. He runs Destroy All Software, which publishes advanced screencasts for serious developers covering Unix, OO design, TDD, and dynamic languages.\",\n"
        + "      \"created_at\": \"2016-02-02T20:32:20.498-05:00\",\n"
        + "      \"event_id\": 2,\n"
        + "      \"id\": 62,\n"
        + "      \"name\": \"Gary Bernhardt\",\n"
        + "      \"photo\": \"https://s3.amazonaws.com/selfconf/speakers/gary-bernhardt.png\",\n"
        + "      \"twitter\": \"garybernhardt\",\n"
        + "      \"updated_at\": \"2016-02-02T21:00:26.659-05:00\"\n"
        + "    }\n"
        + "  ],\n"
        + "  \"updated_at\": \"2016-03-06T18:40:32.797-05:00\"\n"
        + "}";
  }
}
