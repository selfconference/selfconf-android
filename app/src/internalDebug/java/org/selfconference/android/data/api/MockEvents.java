package org.selfconference.android.data.api;

import com.google.common.collect.ImmutableList;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.util.DateTimes;

import static org.selfconference.android.data.api.MockOrganizers.JOE_ROBIT;
import static org.selfconference.android.data.api.MockOrganizers.PAT_BOT;
import static org.selfconference.android.data.api.MockOrganizers.ROBO_SCOTT;

final class MockEvents {

  static final Event SELF_CONF_2016 = Event.builder() //
      .id(2) //
      .startDate(DateTimes.parseEst("2016-05-20T08:00:00.000-05:00")) //
      .endDate(DateTimes.parseEst("2016-05-21T18:00:00.000-05:00")) //
      .ticketsLink("http://selfconf2016.eventbrite.com") //
      .about("Self.conference 2016 is a fun place to be.") //
      .organizers(ImmutableList.of(JOE_ROBIT, ROBO_SCOTT, PAT_BOT)) //
      .build();

  private MockEvents() {
    throw new AssertionError("No instances.");
  }
}
