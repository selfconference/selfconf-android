package org.selfconference.android.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.selfconference.android.App;
import org.selfconference.android.R;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.api.Speaker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.StringObservable;

import static android.view.View.OnClickListener;

public class SessionsAdapter extends RecyclerView.Adapter<SessionViewHolder> {
    public interface Callback {
        void onSessionSelected(View view, Session event);
    }

    @Inject
    SelfConferenceApi api;

    private final Callback callback;

    public SessionsAdapter(Callback callback) {
        App.getInstance().inject(this);
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
    public void onBindViewHolder(final SessionViewHolder holder, final int position) {
        final Session session = sessions.get(position);
        final Observable<String> nameObservable = api.getSpeakersForSession(session)
                .flatMap(new Func1<List<Speaker>, Observable<Speaker>>() {
                    @Override
                    public Observable<Speaker> call(List<Speaker> speakers) {
                        return Observable.from(speakers);
                    }
                })
                .flatMap(new Func1<Speaker, Observable<String>>() {
                    @Override
                    public Observable<String> call(Speaker speaker) {
                        return Observable.just(speaker.getName());
                    }
                });


        StringObservable.join(nameObservable, ", ")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        holder.sessionName.setText(s);
                    }
                });

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
