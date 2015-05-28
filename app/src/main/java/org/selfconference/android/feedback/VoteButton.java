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
import org.selfconference.android.brand.BrandColor;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v4.graphics.drawable.DrawableCompat.setTint;
import static android.support.v4.graphics.drawable.DrawableCompat.wrap;
import static org.selfconference.android.feedback.Vote.NEGATIVE;
import static org.selfconference.android.feedback.Vote.POSITIVE;
import static org.selfconference.android.utils.ResourceProvider.getColor;

/**
 * A wrapper view for providing thumbs up or thumbs down feedback for a session.
 * <p/>
 * In order to use this view, you must abide to a contact by calling
 * {@link #setOnVoteSelectedListener(OnVoteSelectedListener)} and
 * {@link #setBrandColor(BrandColor)}.
 * <p/>
 * <pre>
 * VoteButton voteButton = (VoteButton) findViewById(R.id.vote_button);
 * voteButton.setOnVoteSelectedListener(this);
 * voteButton.setBrandColor(session.getBrandColor());
 * </pre>
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
    private BrandColor brandColor;

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
            thumbsDownView.setTag(NEGATIVE);
            thumbsUpView.setTag(POSITIVE);

            deselect(thumbsDownView);
            deselect(thumbsUpView);

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

    /**
     * Set a brand color for this button.
     * This color will be used by the thumb images as the selected color.
     * The brand color should be passed in from the session associated with this button.
     *
     * @param brandColor The brand color for the thumbs. Must not be {@code null}.
     */
    public void setBrandColor(@NonNull BrandColor brandColor) {
        this.brandColor = brandColor;
    }

    @Override public void onClick(@NonNull View v) {
        if (v == thumbsDownView) {
            notifyClicked((Vote) thumbsDownView.getTag());
            select(thumbsDownView);
            deselect(thumbsUpView);
        } else {
            notifyClicked((Vote) thumbsUpView.getTag());
            select(thumbsUpView);
            deselect(thumbsDownView);
        }
    }

    private void notifyClicked(Vote feedback) {
        if (onVoteSelectedListener != null) {
            onVoteSelectedListener.onVoteSelected(this, feedback);
        }
    }

    private void select(ImageView view) {
        applyColor(view, brandColor.getPrimary());
    }

    private void deselect(ImageView view) {
        applyColor(view, getColor(R.color.image_tint_dark));
    }

    private static void applyColor(ImageView view, int color) {
        final Drawable wrappedDrawable = wrap(view.getDrawable());
        setTint(wrappedDrawable, color);
        view.setImageDrawable(wrappedDrawable);
    }
}
