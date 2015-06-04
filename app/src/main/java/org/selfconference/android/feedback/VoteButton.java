package org.selfconference.android.feedback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v4.graphics.drawable.DrawableCompat.setTint;
import static android.support.v4.graphics.drawable.DrawableCompat.wrap;
import static org.selfconference.android.feedback.Vote.NEGATIVE;
import static org.selfconference.android.feedback.Vote.POSITIVE;
import static org.selfconference.android.utils.ResourceProvider.getColor;

/**
 * A wrapper view for providing thumbs up or thumbs down feedback for a session.
 */
public class VoteButton extends LinearLayout implements OnClickListener {
    /**
     * The interface definition for a callback to be invoked when a vote is selected
     */
    public interface OnVoteSelectedListener {
        /**
         * Called when a vote has been selected
         *
         * @param voteButton The VoteButton that was clicked
         * @param vote       The type of vote that was selected
         */
        void onVoteSelected(VoteButton voteButton, Vote vote);
    }

    @InjectView(R.id.thumbs_down_view) ImageView thumbsDownView;
    @InjectView(R.id.thumbs_up_view) ImageView thumbsUpView;

    private OnVoteSelectedListener onVoteSelectedListener;

    public VoteButton(Context context) {
        this(context, null, 0);
    }

    public VoteButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_thumbs_up_button, this);
        ButterKnife.inject(this);

        setOrientation(HORIZONTAL);

        if (!isInEditMode()) {
            applyColor(thumbsDownView, thumbsUpView);

            thumbsDownView.setOnClickListener(this);
            thumbsUpView.setOnClickListener(this);
        }
    }

    /**
     * Register a callback to be invoked when a vote has been selected
     *
     * @param onVoteSelectedListener The callback that will be run
     */
    public void setOnVoteSelectedListener(OnVoteSelectedListener onVoteSelectedListener) {
        this.onVoteSelectedListener = onVoteSelectedListener;
    }

    @Override public void onClick(@NonNull View v) {
        if (v == thumbsDownView) {
            notifyClicked(NEGATIVE);
        } else {
            notifyClicked(POSITIVE);
        }
    }

    private void notifyClicked(Vote feedback) {
        if (onVoteSelectedListener != null) {
            onVoteSelectedListener.onVoteSelected(this, feedback);
        }
    }

    private static void applyColor(ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            final Drawable wrappedDrawable = wrap(imageView.getDrawable());
            setTint(wrappedDrawable, getColor(R.color.image_tint_dark));
            imageView.setImageDrawable(wrappedDrawable);
        }
    }
}
