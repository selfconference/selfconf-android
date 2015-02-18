package org.selfconference.android.speakers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpeakerViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.speaker_name)
    public TextView speakerName;

    @InjectView(R.id.speaker_headshot)
    public ImageView speakerHeadshot;

    public SpeakerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
