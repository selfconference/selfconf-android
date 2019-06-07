package org.selfconference.data.api;

import com.google.common.collect.ImmutableList;
import org.selfconference.data.api.model.Event;
import org.selfconference.util.Instants;

import static org.selfconference.data.api.MockOrganizers.JOE_ROBIT;
import static org.selfconference.data.api.MockOrganizers.PAT_BOT;
import static org.selfconference.data.api.MockOrganizers.ROBO_SCOTT;

final class MockEvents {

  static final Event SELF_CONF_2016 = Event.builder() //
      .id(2) //
      .start_date(Instants.fromEstString("2016-05-20T08:00:00.000-05:00")) //
      .end_date(Instants.fromEstString("2016-05-21T18:00:00.000-05:00")) //
      .tickets_link("http://selfconf2016.eventbrite.com") //
      .about("Self.conference 2016 is a fun place to be.") //
      .organizers(ImmutableList.of(JOE_ROBIT, ROBO_SCOTT, PAT_BOT)) //
      .build();

  private MockEvents() {
    throw new AssertionError("No instances.");
  }
}
