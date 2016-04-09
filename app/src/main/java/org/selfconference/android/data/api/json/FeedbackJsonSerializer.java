package org.selfconference.android.data.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.selfconference.android.data.api.model.Feedback;

public final class FeedbackJsonSerializer implements JsonSerializer<Feedback> {
  private static final String KEY_COMMENTS = "comments";
  private static final String KEY_VOTE = "vote";

  @Override public JsonElement serialize(Feedback feedback, Type typeOfSrc,
      JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(KEY_COMMENTS, feedback.comments());
    jsonObject.addProperty(KEY_VOTE, feedback.vote().toInt());
    return jsonObject;
  }
}
