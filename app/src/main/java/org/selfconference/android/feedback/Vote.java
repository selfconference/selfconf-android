package org.selfconference.android.feedback;

/**
 * Two feedback options used to submit speaker feedback
 */
public enum Vote {
  /**
   * Positive feedback
   * <p/>
   * Visually represented as a thumbs up in {@link FeedbackFragment}
   * Serialized to JSON as {@code 1} in {@link VoteTypeAdapter}
   */
  POSITIVE,

  /**
   * Negative feedback
   * <p/>
   * Visually represented as a thumbs down in {@link FeedbackFragment}
   * Serialized to JSON as {@code -1} in {@link VoteTypeAdapter}
   */
  NEGATIVE
}
