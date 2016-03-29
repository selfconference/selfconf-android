package org.selfconference.android.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class RoomJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomJsonDeserializer()).create();
  }

  @Test public void deserializesNullRoom() {
    Room room = gson.fromJson("{}", Room.class);

    assertThat(room).isEqualTo(Room.emptyRoom());
  }

  @Test public void deserializesValidRoom() {
    Room room = gson.fromJson(validRoomJson(), Room.class);

    assertThat(room).isEqualTo(Room.create(7, "Salon A"));
  }

  private static String validRoomJson() {
    return "{\n"
        + "  \"created_at\": \"2015-07-23T13:24:47.094-05:00\",\n"
        + "  \"event_id\": 2,\n"
        + "  \"id\": 7,\n"
        + "  \"name\": \"Salon A\",\n"
        + "  \"order\": 0,\n"
        + "  \"updated_at\": \"2016-03-03T00:56:47.320-05:00\",\n"
        + "  \"venue_id\": 1\n"
        + "}";
  }
}
