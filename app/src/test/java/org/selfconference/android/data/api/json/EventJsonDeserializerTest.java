package org.selfconference.android.data.api.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerTypeAdapter;

import static org.assertj.core.api.Assertions.assertThat;

public final class EventJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder() //
        .registerTypeAdapter(Event.class, new EventJsonDeserializer())
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter())
        .create();
  }

  @Test public void parsesEventJsonProperly() {
    Event event = gson.fromJson(eventJson(), Event.class);

    assertThat(event).isEqualTo(Event.builder()
        .id(2)
        .about("Self.conference is a fantastic conference.")
        .ticketsLink("http://selfconf2016.eventbrite.com")
        .startDate(new DateTime(2016, 5, 20, 8, 0, 0, 0, DateTimeZone.forOffsetHours(-5)))
        .endDate(new DateTime(2016, 5, 21, 18, 0, 0, 0, DateTimeZone.forOffsetHours(-5)))
        .organizers(ImmutableList.of(Speaker.builder()
            .id(1)
            .name("Amber Conville")
            .bio("Amber Conville is a developer.")
            .twitter("crebma")
            .photo("http://s3.amazonaws.com/selfconf/organizers/amber.jpg")
            .addSessions(ImmutableList.of())
            .build()))
        .build());
  }

  private static String eventJson() {
    return "{\n"
        + "  \"id\": 2,\n"
        + "  \"venue_id\": 1,\n"
        + "  \"name\": \"self.conference\",\n"
        + "  \"about\": \"Self.conference is a fantastic conference.\",\n"
        + "  \"twitter\": \"selfconference\",\n"
        + "  \"lanyard\": \"2016/selfconference\",\n"
        + "  \"tickets_link\": \"http://selfconf2016.eventbrite.com\",\n"
        + "  \"tickets_iframe_link\": \"http://eventbrite.com/tickets-external?eid=20688851913\\u0026ref=etckt\",\n"
        + "  \"start_date\": \"2016-05-20T08:00:00.000-05:00\",\n"
        + "  \"end_date\": \"2016-05-21T18:00:00.000-05:00\",\n"
        + "  \"created_at\": \"2015-07-23T13:24:47.030-05:00\",\n"
        + "  \"updated_at\": \"2016-03-15T13:56:03.161-05:00\",\n"
        + "  \"extra\": \"\",\n"
        + "  \"sessions_published\": false,\n"
        + "  \"submissions_start\": \"2016-01-18T13:00:00.000-05:00\",\n"
        + "  \"submissions_end\": \"2016-02-15T23:59:59.000-05:00\",\n"
        + "  \"scholarships_start\": \"2016-03-13T19:00:00.000-05:00\",\n"
        + "  \"scholarships_end\": \"2016-03-28T18:59:00.000-05:00\",\n"
        + "  \"scholarships_announce\": \"2016-04-04T05:00:00.000-05:00\",\n"
        + "  \"organizers\": [\n"
        + "    {\n"
        + "      \"id\": 1,\n"
        + "      \"name\": \"Amber Conville\",\n"
        + "      \"bio\": \"Amber Conville is a developer.\",\n"
        + "      \"email\": \"amber@selfconference.org\",\n"
        + "      \"twitter\": \"crebma\",\n"
        + "      \"photo\": \"http://s3.amazonaws.com/selfconf/organizers/amber.jpg\",\n"
        + "      \"created_at\": \"2015-03-02T09:50:48.779-05:00\",\n"
        + "      \"updated_at\": \"2015-10-28T12:04:07.726-05:00\"\n"
        + "    }\n"
        + "  ]\n"
        + "}";
  }
}
