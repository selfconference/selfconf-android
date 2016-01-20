package org.selfconference.android.feedback;

import org.selfconference.android.api.SelfConferenceClient;

/**
 * A wrapper object used to make a feedback POST request
 * <p/>
 * {@link SelfConferenceClient#submitFeedback(int, FeedbackRequest)} needs the following body:
 * <p/>
 * <pre>
 * {
 *     "feedback": {
 *         "vote": 1,
 *         "comments": "comments go here"
 *     }
 * }
 * </pre>
 * <p/>
 * This object allows Gson to serialize the request as above.
 */
@SuppressWarnings({ "FieldCanBeLocal", "unused" }) //
public final class FeedbackRequest {
  private final Feedback feedback;

  public FeedbackRequest(Feedback feedback) {
    this.feedback = feedback;
  }
}
