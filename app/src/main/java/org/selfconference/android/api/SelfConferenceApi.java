package org.selfconference.android.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.selfconference.android.App;
import org.selfconference.android.utils.DateTimeHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static rx.Observable.OnSubscribe;

public class SelfConferenceApi {
    private static final Type LIST_SESSION_TYPE = new TypeToken<List<Session>>() {}.getType();
    private static final Type LIST_SPEAKER_TYPE = new TypeToken<List<Speaker>>() {}.getType();

    @Inject
    Lazy<Gson> gson;

    public SelfConferenceApi() {
        App.getInstance().inject(this);
    }

    public Observable<List<Session>> getSchedule() {
        return Observable.create(new OnSubscribe<List<Session>>() {
            @Override
            public void call(Subscriber<? super List<Session>> subscriber) {
                try {
                    final List<Session> events = loadFile("test-session.json", LIST_SESSION_TYPE);
                    subscriber.onNext(events);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<List<Session>> getScheduleByDay(final Day day) {
        return getSchedule()
                .flatMap(new Func1<List<Session>, Observable<Session>>() {
                    @Override
                    public Observable<Session> call(List<Session> sessions) {
                        return Observable.from(sessions);
                    }
                })
                .filter(new Func1<Session, Boolean>() {
                    @Override
                    public Boolean call(Session session) {
                        final Interval interval = DateTimeHelper.intervalForDay(day);
                        return interval.contains(session.getBeginning());
                    }
                }).toList();
    }

    public Observable<List<Speaker>> getSpeakers() {
        return Observable.create(new OnSubscribe<List<Speaker>>() {
            @Override
            public void call(Subscriber<? super List<Speaker>> subscriber) {
                try {
                    final List<Speaker> speakers = loadFile("test-speaker.json", LIST_SPEAKER_TYPE);
                    subscriber.onNext(speakers);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private <T> T loadFile(String filename, Type typeOfT) throws IOException {
        final InputStream json = App.getInstance().getAssets().open(filename);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        return gson.get().fromJson(reader, typeOfT);
    }
}
