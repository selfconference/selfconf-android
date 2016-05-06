package org.selfconference.android.data.api.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Organizer;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.support.file.Events;
import org.selfconference.android.util.Instants;

import static org.assertj.core.api.Assertions.assertThat;

public final class EventJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder() //
        .registerTypeAdapter(Event.class, new EventJsonDeserializer())
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter())
        .registerTypeAdapter(Organizer.class, new OrganizerJsonDeserializer())
        .create();
  }

  @Test public void parsesEventJsonProperly() {
    Event event = gson.fromJson(Events.eventJson(), Event.class);

    assertThat(event).isEqualTo(Event.builder()
        .id(2)
        .about("Self.conference is a fantastic conference.")
        .tickets_link("http://selfconf2016.eventbrite.com")
        .start_date(Instants.fromEstString("2016-05-20T08:00:00.000-05:00"))
        .end_date(Instants.fromEstString("2016-05-21T18:00:00.000-05:00"))
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
