package org.selfconference.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import org.selfconference.R;
import org.selfconference.data.api.model.Vote;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import static androidx.core.graphics.drawable.DrawableCompat.setTint;
import static androidx.core.graphics.drawable.DrawableCompat.wrap;

/**
 * A wrapper view for providing thumbs up or thumbs down feedback for a session.
 */
public final class VoteButton extends LinearLayout implements OnClickListener {

  /**
   * The interface definition for a callback to be invoked when a vote is selected.
   */
  public interface OnVoteSelectedListener {
    /**
     * Called when a vote has been selected.
     *
     * @param voteButton The {@link VoteButton} that was clicked
     * @param vote The type of vote that was selected
     */
    void onVoteSelected(VoteButton voteButton, Vote vote);
  }

  @BindView(R.id.thumbs_down_view) ImageView thumbsDownView;
  @BindView(R.id.thumbs_up_view) ImageView thumbsUpView;
  @BindColor(R.color.image_tint_dark) int darkTint;

  private OnVoteSelectedListener onVoteSelectedListener;

  public VoteButton(Context context) {
    this(context, null);
  }

  public VoteButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    View.inflate(context, R.layout.view_thumbs_up_button, this);
    ButterKnife.bind(this);

    setOrientation(HORIZONTAL);

    if (!isInEditMode()) {
      applyTint(thumbsDownView, thumbsUpView);

      thumbsDownView.setOnClickListener(this);
      thumbsUpView.setOnClickListener(this);
    }
  }

  /**
   * Register a callback to be invoked when a vote has been selected
   *
   * @param onVoteSelectedListener The callback that will be invoked
   */
  public void setOnVoteSelectedListener(OnVoteSelectedListener onVoteSelectedListener) {
    this.onVoteSelectedListener = onVoteSelectedListener;
  }

  @Override public void onClick(@NonNull View v) {
    if (v == thumbsDownView) {
      notifyClicked(Vote.NEGATIVE);
    } else {
      notifyClicked(Vote.POSITIVE);
    }
  }

  private void notifyClicked(Vote vote) {
    if (onVoteSelectedListener != null) {
      onVoteSelectedListener.onVoteSelected(this, vote);
    }
  }

  private void applyTint(ImageView... imageViews) {
    for (ImageView imageView : imageViews) {
      Drawable wrappedDrawable = wrap(imageView.getDrawable());
      setTint(wrappedDrawable, darkTint);
      imageView.setImageDrawable(wrappedDrawable);
    }
  }
}
