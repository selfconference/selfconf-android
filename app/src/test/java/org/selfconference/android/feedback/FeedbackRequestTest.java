package org.selfconference.android.feedback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.feedback.VoteButton.VOTE_POSITIVE;

public final class FeedbackRequestTest {
  private static final Gson gson = new GsonBuilder().create();

  @Test public void feedbackRequestSerializedAsExpected() {
    Feedback feedback = new Feedback(VOTE_POSITIVE, "That session was awesome!");
    FeedbackRequest feedbackRequest = new FeedbackRequest(feedback);

    String json = gson.toJson(feedbackRequest);

    assertThat(json) //
        .isEqualTo("{\"feedback\":{\"vote\":1,\"comments\":\"That session was awesome!\"}}");
  }
}
