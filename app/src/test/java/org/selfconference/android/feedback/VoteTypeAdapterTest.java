package org.selfconference.android.feedback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.feedback.Vote.NEGATIVE;
import static org.selfconference.android.feedback.Vote.POSITIVE;

public class VoteTypeAdapterTest {
  private static final Gson gson =
      new GsonBuilder().registerTypeAdapter(Vote.class, new VoteTypeAdapter()).create();

  @Test public void NEGATIVE_serializesTo_negativeOne() {
    final String output = gson.toJson(NEGATIVE);

    assertThat(output).isEqualTo("-1");
  }

  @Test public void POSITIVE_serializesTo_positiveOne() {
    final String output = gson.toJson(POSITIVE);

    assertThat(output).isEqualTo("1");
  }
}