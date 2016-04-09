package org.selfconference.android.data.api.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.selfconference.android.data.api.model.SponsorLevel;

public final class SponsorLevelJsonDeserializer implements JsonDeserializer<SponsorLevel> {
  @Override public SponsorLevel deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();

    return SponsorLevel.builder()
        .id(jsonObject.get("id").getAsInt())
        .name(jsonObject.get("name").getAsString())
        .order(jsonObject.get("order").getAsInt())
        .build();
  }
}
