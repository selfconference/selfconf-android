package org.selfconference.android.feedback;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.selfconference.android.feedback.Vote.POSITIVE;

/**
 * A TypeAdapter used to serialize a {@link Vote} to JSON
 * <p/>
 * {@link Vote#POSITIVE} serializes to {@code 1}.
 * {@link Vote#NEGATIVE} serializes to {@code -1}.
 */
public final class VoteTypeAdapter extends TypeAdapter<Vote> {
  @Override public void write(JsonWriter out, Vote value) throws IOException {
    out.value(value == POSITIVE ? 1 : -1);
  }

  @Override public Vote read(JsonReader in) throws IOException {
    return null;
  }
}
