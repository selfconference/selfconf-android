package org.selfconference.android.data.api.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.selfconference.android.data.api.model.Organizer;

public final class OrganizerJsonDeserializer implements JsonDeserializer<Organizer> {
  @Override
  public Organizer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();

    return Organizer.builder() //
        .id(jsonObject.get("id").getAsInt()) //
        .bio(jsonObject.get("bio").getAsString()) //
        .photo(jsonObject.get("photo").getAsString()) //
        .name(jsonObject.get("name").getAsString()) //
        .email(jsonObject.get("email").getAsString()) //
        .twitter(jsonObject.get("twitter").getAsString()) //
        .build();
  }
}
