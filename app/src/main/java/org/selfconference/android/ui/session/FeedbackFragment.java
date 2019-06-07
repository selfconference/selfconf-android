package org.selfconference.ui.session;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.selfconference.R;
import org.selfconference.data.api.model.Feedback;
import org.selfconference.data.api.model.Session;
import org.selfconference.data.api.model.Vote;
import org.selfconference.ui.view.VoteButton;
import org.selfconference.ui.view.VoteButton.OnVoteSelectedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@code DialogFragment} used to provide feedback for a session.
 * <p/>
 * Contains a {@link VoteButton} used for providing thumbs up or thumbs down feedback.
 * Also contains an {@link EditText} for optional comments.
 */
public final class FeedbackFragment extends DialogFragment implements OnVoteSelectedListener {
  public interface OnFeedbackCreatedListener {
    void onFeedbackCreated(Session session, Feedback feedback);
  }

  public static final String TAG = FeedbackFragment.class.getName();
  private static final String EXTRA_SESSION =
      "org.selfconference.ui.session.FeedbackFragment.EXTRA_SESSION";

  @BindView(R.id.vote_button) VoteButton voteButton;
  @BindView(R.id.feedback_fragment_comment_section) EditText comments;

  private Session session;
  private OnFeedbackCreatedListener onFeedbackCreatedListener;

  /**
   * A factory method used to create a {@link FeedbackFragment} for a session
   *
   * @param session the session to give feedback for. Must not be {@code null}.
   * @return a FeedbackFragment for the provided session.
   */
  public static FeedbackFragment newInstance(@NonNull Session session) {
    checkNotNull(session, "session == null");
    Bundle args = new Bundle(1);
    args.putParcelable(EXTRA_SESSION, session);

    FeedbackFragment feedbackFragment = new FeedbackFragment();
    feedbackFragment.setArguments(args);
    return feedbackFragment;
  }

  @Override public void onAttach(Context context) {
    try {
      onFeedbackCreatedListener = (OnFeedbackCreatedListener) context;
    } catch (ClassCastException e) {
      throw new RuntimeException(e);
    }
    super.onAttach(context);
  }

  @Override public void onDetach() {
    super.onDetach();
    onFeedbackCreatedListener = null;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    session = checkNotNull((Session) getArguments().getParcelable(EXTRA_SESSION));

    voteButton.setOnVoteSelectedListener(this);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_feedback, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
    getDialog().setTitle(R.string.feedback_dialog_title);
    getDialog().getWindow().getAttributes().windowAnimations = R.style.FeedbackFragment;
  }

  @Override public void onVoteSelected(VoteButton voteButton, Vote vote) {
    if (onFeedbackCreatedListener != null) {
      Feedback feedback = Feedback.builder()
          .vote(vote)
          .comments(comments.getText().toString())
          .build();
      onFeedbackCreatedListener.onFeedbackCreated(session, feedback);
    }

    voteButton.postDelayed(this::dismiss, 200);
  }
}
