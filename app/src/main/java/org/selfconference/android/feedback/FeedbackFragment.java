package org.selfconference.android.feedback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.birbit.android.jobqueue.JobManager;
import javax.inject.Inject;
import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.data.jobs.SubmitFeedbackJob;
import org.selfconference.android.feedback.VoteButton.OnVoteSelectedListener;
import org.selfconference.android.feedback.VoteButton.Vote;
import org.selfconference.android.data.api.model.Session;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@code DialogFragment} used to provide feedback for a session.
 * <p/>
 * Contains a {@link VoteButton} used for providing thumbs up or thumbs down feedback.
 * Also contains an {@link EditText} for optional comments.
 */
public final class FeedbackFragment extends DialogFragment implements OnVoteSelectedListener {
  public static final String TAG = FeedbackFragment.class.getName();
  private static final String EXTRA_SESSION =
      "org.selfconference.android.feedback.FeedbackFragment.EXTRA_SESSION";

  @Inject JobManager jobManager;

  @Bind(R.id.vote_button) VoteButton voteButton;
  @Bind(R.id.feedback_fragment_comment_section) EditText comments;

  private Session session;

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

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    App.getInstance().inject(this);
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

  @Override public void onVoteSelected(VoteButton voteButton, @Vote int vote) {
    Feedback feedback = new Feedback(vote, comments.getText().toString());
    jobManager.addJobInBackground(new SubmitFeedbackJob(session, feedback));

    voteButton.postDelayed(this::dismiss, 200);
  }
}
