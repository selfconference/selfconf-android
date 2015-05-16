package org.selfconference.android.api;

import org.selfconference.android.session.Day;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.sponsors.Sponsor;

import java.util.List;

import rx.Observable;

public interface Api {
    Observable<List<Session>> getSessions();
    Observable<List<Session>> getSessionsByDay(final Day day);
    Observable<Session> getSessionById(final int id);
    Observable<List<Speaker>> getSpeakers();
    Observable<List<Sponsor>> getSponsors();
}
