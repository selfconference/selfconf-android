package org.selfconference.android.session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.selfconference.android.support.Rooms;

import static org.assertj.core.api.Assertions.assertThat;

public final class RoomJsonDeserializerTest {

  private Gson gson;

  @Before public void setUp() {
    gson = new GsonBuilder().registerTypeAdapter(Room.class, new RoomJsonDeserializer()).create();
  }

  @Test public void deserializesNullRoom() {
    Room room = gson.fromJson("{}", Room.class);

    assertThat(room).isEqualTo(Room.nullRoom());
  }

  @Test public void deserializesValidRoom() {
    Room room = gson.fromJson(Rooms.room(), Room.class);

    assertThat(room).isEqualTo(Room.create(7, "Salon A"));
  }
}
