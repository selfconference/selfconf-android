package org.selfconference.android.speakers;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.utils.PlaceholderDrawable;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.InjectView;
import rx.functions.Func1;

import static android.text.Html.fromHtml;
import static android.view.View.GONE;
import static android.view.ViewTreeObserver.OnPreDrawListener;

public class SpeakerAdapter extends FilterableAdapter<Speaker, SpeakerAdapter.SpeakerViewHolder> {
    public interface OnSpeakerClickListener {
        void onSpeakerClick(Speaker speaker);
    }

    @Inject Picasso picasso;

    private OnSpeakerClickListener onSpeakerClickListener;
    private final boolean showDescription;

    public SpeakerAdapter(boolean showDescription) {
        super();
        this.showDescription = showDescription;
    }

    public void setOnSpeakerClickListener(OnSpeakerClickListener onSpeakerClickListener) {
        this.onSpeakerClickListener = onSpeakerClickListener;
    }

    @Override protected Func1<Speaker, Boolean> filterPredicate(final String query) {
        return new Func1<Speaker, Boolean>() {
            @Override public Boolean call(Speaker speaker) {
                return speaker.getName()
                        .toLowerCase(Locale.US)
                        .contains(query.toLowerCase(Locale.US));
            }
        };
    }

    @Override public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_row_single_item_with_avatar, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override public void onBindViewHolder(final SpeakerViewHolder holder, final int position) {
        final Speaker speaker = getFilteredData().get(position);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(@NonNull View v) {
                if (onSpeakerClickListener != null) {
                    onSpeakerClickListener.onSpeakerClick(speaker);
                }
            }
        });
        holder.speakerName.setText(speaker.getName());

        holder.itemView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override public boolean onPreDraw() {
                holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);

                picasso.load(speaker.getPhoto())
                        .resize(holder.speakerPhoto.getWidth(), holder.speakerPhoto.getHeight())
                        .centerCrop()
                        .transform(new CircularTransformation(speaker.getPhoto()))
                        .placeholder(PlaceholderDrawable.forSpeaker(speaker))
                        .into(holder.speakerPhoto);
                return true;
            }
        });
        if (showDescription) {
            holder.speakerDescription.setText(fromHtml(speaker.getBio()));
        } else {
            holder.speakerDescription.setVisibility(GONE);
        }
    }

    public static class SpeakerViewHolder extends ButterKnifeViewHolder {

        @InjectView(R.id.row_icon) public ImageView speakerPhoto;
        @InjectView(R.id.row_title) public TextView speakerName;
        @InjectView(R.id.row_subtitle) public TextView speakerDescription;

        public SpeakerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
