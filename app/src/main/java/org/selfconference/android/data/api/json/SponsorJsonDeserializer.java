package org.selfconference.android.data.api.json;

import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;

public final class SponsorJsonDeserializer implements JsonDeserializer<Sponsor> {

  @Override
  public Sponsor deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject sponsorObject = json.getAsJsonObject();
    SponsorJsonObjectDecorator decorator = new SponsorJsonObjectDecorator(context, sponsorObject);

    return Sponsor.builder()
        .id(decorator.id())
        .name(decorator.name())
        .link(decorator.link())
        .photo(decorator.photo())
        .addSponsorLevels(decorator.sponsorLevels())
        .build();
  }

  private static final class SponsorJsonObjectDecorator {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LINK = "link";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_SPONSOR_LEVELS = "sponsor_levels";

    private final JsonDeserializationContext context;
    private final JsonObject sponsorObject;

    SponsorJsonObjectDecorator(JsonDeserializationContext context, JsonObject sponsorObject) {
      this.context = context;
      this.sponsorObject = sponsorObject;
    }

    int id() {
      return sponsorObject.get(KEY_ID).getAsInt();
    }

    String name() {
      return sponsorObject.get(KEY_NAME).getAsString();
    }

    String link() {
      return sponsorObject.get(KEY_LINK).getAsString();
    }

    String photo() {
      return sponsorObject.get(KEY_PHOTO).getAsString();
    }

    Iterable<SponsorLevel> sponsorLevels() {
      JsonArray sponsorLevelsArray = sponsorObject.get(KEY_SPONSOR_LEVELS).getAsJsonArray();
      return Iterables.transform(sponsorLevelsArray, input -> {
        JsonObject sponsorLevel = input.getAsJsonObject();
        return context.deserialize(sponsorLevel, SponsorLevel.class);
      });
    }
  }
}