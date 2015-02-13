package org.selfconference.android.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.selfconference.android.R;
import org.selfconference.android.api.Session;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public class SessionsAdapter extends RecyclerView.Adapter<SessionViewHolder> {
    public interface Callback {
        void onSessionSelected(View view, Session event);
    }

    private final Callback callback;

    public SessionsAdapter(Callback callback) {
        this.callback = callback;
    }

    private final List<Session> sessions = new ArrayList<>();

    public void setSessions(List<Session> events) {
        this.sessions.clear();
        this.sessions.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_schedule_event_row, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        final Session session = sessions.get(position);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                callback.onSessionSelected(v, session);
            }
        });

        holder.sessionTitle.setText(session.getTitle());
        holder.sessionRoom.setText(session.getRoom());
        holder.sessionTime.setText(session.getBeginning().toString("M/dd h:mm a"));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
