package org.selfconference.android.speakers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpeakerViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.row_icon)
    public ImageView speakerHeadshot;

    @InjectView(R.id.row_title)
    public TextView speakerName;

    public SpeakerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
