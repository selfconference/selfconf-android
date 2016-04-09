package org.selfconference.android.data.api.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.selfconference.android.CustomTestRunner;
import org.selfconference.android.Parceler.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.selfconference.android.Parceler.testParceling;

@RunWith(CustomTestRunner.class) //
public final class FeedbackTest {

  @Test public void parcelsNegativeFeedbackWithoutError() {
    Feedback feedback = Feedback.builder() //
        .vote(Vote.NEGATIVE) //
        .comments("test") //
        .build();

    Container<Feedback> feedbackContainer = testParceling(feedback, AutoValue_Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }

  @Test public void parcelsPositiveFeedbackWithoutError() {
    Feedback feedback = Feedback.builder() //
        .vote(Vote.POSITIVE) //
        .comments("test") //
        .build();

    Container<Feedback> feedbackContainer = testParceling(feedback, AutoValue_Feedback.CREATOR);

    assertThat(feedbackContainer.original).isEqualTo(feedbackContainer.parceled);
  }
}
