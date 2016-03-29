package org.selfconference.android.session;

import android.support.annotation.Nullable;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.utils.DateTimeHelper;

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
        .beginning(decorator.beginning())
        .description(decorator.description())
        .keynote(decorator.keynote())
        .speakers(decorator.speakers())
        .build();
  }

  private static final class SessionJsonObjectDecorator {
    private final JsonDeserializationContext context;
    private final JsonObject jsonObject;

    SessionJsonObjectDecorator(JsonDeserializationContext context, JsonObject jsonObject) {
      this.context = context;
      this.jsonObject = jsonObject;
    }

    int id() {
      return jsonObject.get("id").getAsInt();
    }

    String title() {
      return jsonObject.get("name").getAsString();
    }

    Room room() {
      JsonElement roomJsonObject = jsonObject.get("room");
      Room room = context.deserialize(roomJsonObject, Room.class);
      return Optional.fromNullable(room).or(Room.emptyRoom());
    }

    @Nullable DateTime beginning() {
      try {
        JsonElement beginning = jsonObject.get("slot");
        return DateTimeHelper.parseDateTime(beginning.getAsString());
      } catch (Exception e) {
        return null;
      }
    }

    String description() {
      return jsonObject.get("abstract").getAsString();
    }

    boolean keynote() {
      JsonElement keynote = jsonObject.get("keynote");
      return keynote != null &&
          !keynote.isJsonNull() &&
          keynote.getAsBoolean();
    }

    ImmutableList<Speaker> speakers() {
      JsonElement speakersElement = jsonObject.get("speakers");
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
