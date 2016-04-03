package org.selfconference.android.speakers;

import android.support.annotation.NonNull;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.List;
import org.selfconference.android.session.Room;
import org.selfconference.android.session.RoomJsonDeserializer;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionJsonDeserializer;

public final class SpeakerTypeAdapter extends TypeAdapter<Speaker> {

  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "name";
  private static final String KEY_BIO = "bio";
  private static final String KEY_PHOTO = "photo";
  private static final String KEY_TWITTER = "twitter";
  private static final String KEY_SESSIONS = "sessions";

  private static final Gson GSON = new GsonBuilder() //
      .registerTypeAdapter(Session.class, new SessionJsonDeserializer())
      .registerTypeAdapter(Room.class, new RoomJsonDeserializer())
      .create();

  @Override public void write(JsonWriter out, Speaker value) throws IOException {

  }

  @Override public Speaker read(JsonReader in) throws IOException {
    Speaker.Builder builder = Speaker.builder();

    in.beginObject();
    while (in.hasNext()) {
      switch (in.nextName()) {
        case KEY_ID:
          builder.id(in.nextInt());
          break;
        case KEY_NAME:
          builder.name(in.nextString());
          break;
        case KEY_BIO:
          String bio = parseNullableString(in);
          builder.bio(bio);
          break;
        case KEY_PHOTO:
          builder.photo(in.nextString());
          break;
        case KEY_TWITTER:
          builder.twitter(in.nextString());
          break;
        case KEY_SESSIONS:
          if (in.peek() == JsonToken.BEGIN_ARRAY) {
            List<Session> sessions = GSON.fromJson(in, new TypeToken<List<Session>>() {
            }.getType());
            builder.addSessions(sessions);
          }
          break;
        default:
          in.skipValue();
          break;
      }
    }
    in.endObject();
    return builder.build();
  }

  @NonNull private static String parseNullableString(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return "";
    }
    return in.nextString().trim();
  }
}
