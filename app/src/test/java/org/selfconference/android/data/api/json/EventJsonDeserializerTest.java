package org.selfconference.android.data.api.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Organizer;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.support.file.Events;

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
    Event event = gson.fromJson(Events.eventJson(), Event.class);

    assertThat(event).isEqualTo(Event.builder()
        .id(2)
        .about("Self.conference is a fantastic conference.")
        .ticketsLink("http://selfconf2016.eventbrite.com")
        .startDate(new DateTime(2016, 5, 20, 8, 0, 0, 0, DateTimeZone.forOffsetHours(-5)))
        .endDate(new DateTime(2016, 5, 21, 18, 0, 0, 0, DateTimeZone.forOffsetHours(-5)))
        .organizers(ImmutableList.of(Organizer.builder()
            .id(1)
            .name("Amber Conville")
            .bio("Amber Conville is a developer.")
            .email("amber@selfconference.org")
            .twitter("crebma")
            .photo("http://s3.amazonaws.com/selfconf/organizers/amber.jpg")
            .build()))
        .build());
  }
}
