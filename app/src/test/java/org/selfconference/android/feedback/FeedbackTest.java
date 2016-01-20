package org.selfconference.android.feedback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.Parceler.testParceling;
import static org.selfconference.android.feedback.VoteButton.VOTE_NEGATIVE;
import static org.selfconference.android.feedback.VoteButton.VOTE_POSITIVE;

@RunWith(CustomTestRunner.class) //
public final class FeedbackTest {

  @Test public void parcelsNegativeFeedbackWithoutError() {
    Feedback feedback = new Feedback(VOTE_NEGATIVE, "test comments");

    Container<Feedback> feedbackContainer = testParceling(feedback, Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }

  @Test public void parcelsPositiveFeedbackWithoutError() {
    Feedback feedback = new Feedback(VOTE_POSITIVE, "test comments");

    Container<Feedback> feedbackContainer = testParceling(feedback, Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }
}
