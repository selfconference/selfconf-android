package org.selfconference.android.data.api.json;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Organizer;
import org.selfconference.android.util.Instants;
import org.threeten.bp.Instant;

public final class EventJsonDeserializer implements JsonDeserializer<Event> {
  @Override
  public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    EventJsonObjectDecorator decorator = new EventJsonObjectDecorator(context, jsonObject);

    return Event.builder()
        .id(decorator.id())
        .about(decorator.about())
        .tickets_link(decorator.tickets_link())
        .start_date(decorator.start_date())
        .end_date(decorator.end_date())
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

    String tickets_link() {
      return jsonObject.get(KEY_TICKETS_LINK).getAsString();
    }

    Instant start_date() {
      String startDate = jsonObject.get(KEY_START_DATE).getAsString();
      return Instants.fromEstString(startDate);
    }

    Instant end_date() {
      String endDate = jsonObject.get(KEY_END_DATE).getAsString();
      return Instants.fromEstString(endDate);
    }

    List<Organizer> organizers() {
      JsonArray organizersArray = jsonObject.get(KEY_ORGANIZERS).getAsJsonArray();
      Iterable<Organizer> organizers =
          Iterables.transform(organizersArray, new Function<JsonElement, Organizer>() {
            @Override public Organizer apply(JsonElement input) {
              return context.deserialize(input, Organizer.class);
            }
          });
      return ImmutableList.copyOf(organizers);
    }
  }
}
