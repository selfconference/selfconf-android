package org.selfconference.android.session;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.selfconference.android.App;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.utils.SharedElements;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Func1;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

public class SessionAdapter extends FilterableAdapter<Session, SessionAdapter.SessionViewHolder> {
    public interface OnSessionClickListener {
        void onSessionClick(SharedElements sharedElements, Session event);
    }

    @Inject
    SelfConferenceApi api;

    @Inject
    SavedSessionPreferences preferences;

    private OnSessionClickListener onSessionClickListener;

    public SessionAdapter() {
        App.getInstance().inject(this);
    }

    public void setOnSessionClickListener(OnSessionClickListener onSessionClickListener) {
        this.onSessionClickListener = onSessionClickListener;
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
        final Session session = getFilteredData().get(position);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (onSessionClickListener != null) {
                    final SharedElements sharedElements = new SharedElements.Builder(v.getContext()).build();
                    onSessionClickListener.onSessionClick(sharedElements, session);
                }
            }
        });

        holder.favoriteSessionIndicator.setVisibility(preferences.isFavorite(session) ? VISIBLE : GONE);

        holder.sessionTitle.setText(session.getTitle());
        holder.sessionSubtitle.setText(session.getRoom());
        try {
            final Session previousSession = getFilteredData().get(position - 1);
            if (session.getBeginning().isEqual(previousSession.getBeginning())) {
                holder.startTime.setVisibility(INVISIBLE);
            } else {
                setStartTime(holder, session);
            }
        } catch (IndexOutOfBoundsException e) {
            setStartTime(holder, session);
        }

    }

    @Override
    protected Func1<Session, Boolean> filterPredicate(final String query) {
        return new Func1<Session, Boolean>() {
            @Override
            public Boolean call(Session session) {
                return session.getTitle()
                        .toLowerCase(Locale.US)
                        .contains(query.toLowerCase(Locale.US));
            }
        };
    }

    private static void setStartTime(SessionViewHolder holder, Session session) {
        final DateTime time = session.getBeginning();
        final String timeString = time.toString("ha");
        holder.startTime.setText(timeString.substring(0, timeString.length() - 1));
        holder.startTime.setVisibility(VISIBLE);
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.start_time)
        public TextView startTime;

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
