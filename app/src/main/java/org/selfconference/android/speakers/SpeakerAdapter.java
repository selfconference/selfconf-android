package org.selfconference.android.speakers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.api.Speaker;
import org.selfconference.android.utils.CircularTransformation;

import java.util.List;

import javax.inject.Inject;

import static android.view.ViewTreeObserver.OnPreDrawListener;
import static org.selfconference.android.utils.BrandColors.getColorForPosition;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerViewHolder> {
    public interface OnSpeakerClickListener {
        void onSpeakerClick(Speaker speaker);
    }

    @Inject
    Picasso picasso;

    private final List<Speaker> speakers = Lists.newArrayList();
    private OnSpeakerClickListener onSpeakerClickListener;

    public SpeakerAdapter() {
        App.getInstance().inject(this);
    }

    public void setOnSpeakerClickListener(OnSpeakerClickListener onSpeakerClickListener) {
        this.onSpeakerClickListener = onSpeakerClickListener;
    }

    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_row_single_item_with_avatar, parent, false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SpeakerViewHolder holder, final int position) {
        final Context context = holder.itemView.getContext();
        final Speaker speaker = speakers.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onSpeakerClickListener.onSpeakerClick(speaker);
            }
        });
        holder.speakerName.setText(speaker.getName());

        holder.itemView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.itemView.getViewTreeObserver().removeOnPreDrawListener(this);

                picasso.load(speaker.getHeadshot())
                        .resize(holder.speakerHeadshot.getWidth(), holder.speakerHeadshot.getHeight())
                        .centerCrop()
                        .transform(new CircularTransformation(speaker.getHeadshot()))
                        .placeholder(TextDrawable.builder()
                                        .buildRound(speaker.getName().substring(0, 1), getColorForPosition(context, position))
                        )
                        .into(holder.speakerHeadshot);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers.clear();
        this.speakers.addAll(speakers);
        notifyDataSetChanged();
    }
}
