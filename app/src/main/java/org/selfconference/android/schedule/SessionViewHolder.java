package org.selfconference.android.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SessionViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.schedule_event_name)
    TextView sessionName;

    @InjectView(R.id.schedule_event_title)
    TextView sessionTitle;

    @InjectView(R.id.schedule_event_room)
    TextView sessionRoom;

    @InjectView(R.id.schedule_event_time)
    TextView sessionTime;

    public SessionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
