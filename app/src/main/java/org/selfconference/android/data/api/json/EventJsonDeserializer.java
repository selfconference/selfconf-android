package org.selfconference.android.data.api.json;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.utils.DateTimeHelper;

public final class EventJsonDeserializer implements JsonDeserializer<Event> {
  @Override
  public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    EventJsonObjectDecorator decorator = new EventJsonObjectDecorator(context, jsonObject);

    return Event.builder()
        .id(decorator.id())
        .about(decorator.about())
        .ticketsLink(decorator.ticketsLink())
        .startDate(decorator.startDate())
        .endDate(decorator.endDate())
        .organizers(decorator.organizers())
        .build();
  }

  private static final class EventJsonObjectDecorator {
    private static final String KEY_ID = "id";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_TICKETS_LINK = "tickets_link";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_ORGANIZERS = "organizers";

    private final JsonDeserializationContext context;
    private final JsonObject jsonObject;

    EventJsonObjectDecorator(JsonDeserializationContext context, JsonObject jsonObject) {
      this.context = context;
      this.jsonObject = jsonObject;
    }

    int id() {
      return jsonObject.get(KEY_ID).getAsInt();
    }

    String about() {
      return jsonObject.get(KEY_ABOUT).getAsString();
    }

    String ticketsLink() {
      return jsonObject.get(KEY_TICKETS_LINK).getAsString();
    }

    DateTime startDate() {
      String startDate = jsonObject.get(KEY_START_DATE).getAsString();
      return DateTimeHelper.parseDateTime(startDate);
    }

    DateTime endDate() {
      String endDate = jsonObject.get(KEY_END_DATE).getAsString();
      return DateTimeHelper.parseDateTime(endDate);
    }

    Iterable<Speaker> organizers() {
      JsonArray organizers = jsonObject.get(KEY_ORGANIZERS).getAsJsonArray();
      return Iterables.transform(organizers, input -> context.deserialize(input, Speaker.class));
    }
  }
}
