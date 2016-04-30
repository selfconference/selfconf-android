package org.selfconference.android.data.api.json;

import android.support.annotation.NonNull;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.util.Instants;
import org.threeten.bp.Instant;

public final class SessionJsonDeserializer implements JsonDeserializer<Session> {

  @Override
  public Session deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    SessionJsonObjectDecorator decorator = new SessionJsonObjectDecorator(context, jsonObject);

    return Session.builder()
        .id(decorator.id())
        .title(decorator.title())
        .room(decorator.room())
        .slotTime(decorator.slotTime())
        .description(decorator.description())
        .keynote(decorator.keynote())
        .speakers(decorator.speakers())
        .build();
  }

  private static final class SessionJsonObjectDecorator {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ROOM = "room";
    private static final String KEY_SLOT = "slot";
    private static final String KEY_ABSTRACT = "abstract";
    private static final String KEY_KEYNOTE = "keynote";
    private static final String KEY_SPEAKERS = "speakers";

    private final JsonDeserializationContext context;
    private final JsonObject jsonObject;

    SessionJsonObjectDecorator(JsonDeserializationContext context, JsonObject jsonObject) {
      this.context = context;
      this.jsonObject = jsonObject;
    }

    int id() {
      return jsonObject.get(KEY_ID).getAsInt();
    }

    String title() {
      return jsonObject.get(KEY_NAME).getAsString();
    }

    Room room() {
      JsonElement roomJsonElement = jsonObject.get(KEY_ROOM);
      JsonElement roomJsonObject = Optional.fromNullable(roomJsonElement).or(new JsonObject());
      Room room = context.deserialize(roomJsonObject, Room.class);
      return Optional.fromNullable(room).or(Room.nullRoom());
    }

    @NonNull Instant slotTime() {
      JsonElement slotElement =
          Optional.fromNullable(jsonObject.get(KEY_SLOT)).or(JsonNull.INSTANCE);
      if (slotElement.isJsonPrimitive()) {
        String slotTime = slotElement.getAsString();
        return Instants.fromEstString(slotTime);
      } else if (slotElement.isJsonObject()) {
        JsonObject slotObject = slotElement.getAsJsonObject();
        String slotTime = slotObject.get("time").getAsString();
        return Instants.fromEstString(slotTime);
      }
      return Instant.MIN;
    }

    String description() {
      return jsonObject.get(KEY_ABSTRACT).getAsString();
    }

    boolean keynote() {
      JsonElement keyNoteElement = jsonObject.get(KEY_KEYNOTE);
      JsonElement keynote = Optional.fromNullable(keyNoteElement).or(JsonNull.INSTANCE);
      //noinspection SimplifiableIfStatement
      if (keynote.isJsonNull()) {
        return false;
      }
      return keynote.getAsBoolean();
    }

    ImmutableList<Speaker> speakers() {
      JsonElement speakersElement = jsonObject.get(KEY_SPEAKERS);
      ImmutableList.Builder<Speaker> speakers = new ImmutableList.Builder<>();

      if (speakersElement == null) {
        return speakers.build();
      } else {
        JsonArray jsonArray = speakersElement.getAsJsonArray();
        for (JsonElement element : jsonArray) {
          Speaker speaker = context.deserialize(element, Speaker.class);
          speakers.add(speaker);
        }
        return speakers.build();
      }
    }
  }
}
