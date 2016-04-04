package org.selfconference.android.session;

import android.support.annotation.NonNull;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.joda.time.ReadableDateTime;
import org.selfconference.android.data.api.NullDateTime;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.utils.DateTimes;

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
      JsonElement roomJsonObject =
          Optional.fromNullable(jsonObject.get("room")).or(new JsonObject());
      Room room = context.deserialize(roomJsonObject, Room.class);
      return Optional.fromNullable(room).or(Room.nullRoom());
    }

    @NonNull ReadableDateTime beginning() {
      try {
        String beginning = jsonObject.get("slot").getAsString();
        return DateTimes.parseEst(beginning);
      } catch (Exception e) {
        return NullDateTime.create();
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
