package org.selfconference.android.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.collect.Lists;

import org.joda.time.DateTime;
import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.utils.SharedElements;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionViewHolder> {

    public interface OnSessionClickListener {
        void onSessionClick(SharedElements sharedElements, Session event);
    }

    @Inject
    SelfConferenceApi api;

    @Inject
    SavedSessionPreferences preferences;

    private final List<Session> sessions = Lists.newArrayList();
    private OnSessionClickListener onSessionClickListener;

    public SessionsAdapter() {
        App.getInstance().inject(this);
    }

    public void setOnSessionClickListener(OnSessionClickListener onSessionClickListener) {
        this.onSessionClickListener = onSessionClickListener;
    }

    public void setSessions(List<Session> events) {
        this.sessions.clear();
        this.sessions.addAll(events);
        notifyDataSetChanged();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_session_row, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SessionViewHolder holder, final int position) {
        final Session session = sessions.get(position);

        holder.sessionBackground.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                final SharedElements sharedElements = new SharedElements.Builder(v.getContext()).build();
                onSessionClickListener.onSessionClick(sharedElements, session);
            }
        });

        holder.favoriteSessionIndicator.setVisibility(preferences.isFavorite(session) ? VISIBLE : GONE);

        holder.sessionTitle.setText(session.getTitle());
        holder.sessionSubtitle.setText(session.getRoom());
        try {
            final Session previousSession = sessions.get(position - 1);
            if (session.getBeginning().isEqual(previousSession.getBeginning())) {
                holder.startTimeNumber.setVisibility(INVISIBLE);
            } else {
                setStartTime(holder, session);
            }
        } catch (IndexOutOfBoundsException e) {
            setStartTime(holder, session);
        }

    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private void setStartTime(SessionViewHolder holder, Session session) {
        final DateTime time = session.getBeginning();
        holder.startTimeNumber.setText(time.toString("h"));
        holder.startTimeIndicator.setText(time.toString("a"));
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.box)
        public View sessionBackground;

        @InjectView(R.id.start_time_number)
        public TextView startTimeNumber;

        @InjectView(R.id.start_time_indicator)
        public TextView startTimeIndicator;

        @InjectView(R.id.slot_title)
        public TextView sessionTitle;

        @InjectView(R.id.slot_subtitle)
        public TextView sessionSubtitle;

        @InjectView(R.id.favorite_session_indicator)
        public View favoriteSessionIndicator;

        public SessionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
