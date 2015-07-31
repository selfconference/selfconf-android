package org.selfconference.android.feedback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.Parceler.testParceling;
import static org.selfconference.android.feedback.Vote.NEGATIVE;
import static org.selfconference.android.feedback.Vote.POSITIVE;

@RunWith(CustomTestRunner.class) public class FeedbackTest {

  @Test public void parcelsNegativeFeedbackWithoutError() {
    final Feedback feedback = new Feedback(NEGATIVE, "test comments");

    final Container<Feedback> feedbackContainer = testParceling(feedback, Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }

  @Test public void parcelsPositiveFeedbackWithoutError() {
    final Feedback feedback = new Feedback(POSITIVE, "test comments");

    final Container<Feedback> feedbackContainer = testParceling(feedback, Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }
}