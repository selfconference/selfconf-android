package org.selfconference.data.api.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.selfconference.support.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.support.Parceler.testParceling;

@RunWith(RobolectricTestRunner.class)
public final class FeedbackTest {

  @Test public void parcelsNegativeFeedbackWithoutError() {
    Feedback feedback = Feedback.builder()
        .vote(Vote.NEGATIVE)
        .comments("test")
        .build();

    Container<Feedback> feedbackContainer = testParceling(feedback, AutoValue_Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }

  @Test public void parcelsPositiveFeedbackWithoutError() {
    Feedback feedback = Feedback.builder()
        .vote(Vote.POSITIVE)
        .comments("test")
        .build();

    Container<Feedback> feedbackContainer = testParceling(feedback, AutoValue_Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }
}
