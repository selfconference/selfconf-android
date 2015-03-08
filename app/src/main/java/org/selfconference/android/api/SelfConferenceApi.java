package org.selfconference.android.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.Interval;
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
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static rx.Observable.OnSubscribe;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class SelfConferenceApi {
    private static final Type LIST_SESSION_TYPE = new TypeToken<List<Session>>() {}.getType();
    private static final Type LIST_SPEAKER_TYPE = new TypeToken<List<Speaker>>() {}.getType();

    @Inject
    Lazy<Gson> gson;

    public SelfConferenceApi() {
        App.getInstance().inject(this);
    }

    public Observable<List<Session>> getSessions() {
        return fileObservable("test-session.json", LIST_SESSION_TYPE);
    }

    public Observable<List<Session>> getSessionsByDay(final Day day) {
        return getSessions()
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
                })
                .toSortedList(sortByDateFunction());
    }

    public Observable<List<Session>> getSessionsForSpeaker(final Speaker speaker) {
        return getSessions()
                .flatMap(new Func1<List<Session>, Observable<Session>>() {
                    @Override
                    public Observable<Session> call(List<Session> sessions) {
                        return Observable.from(sessions);
                    }
                })
                .filter(new Func1<Session, Boolean>() {
                    @Override
                    public Boolean call(Session session) {
                        return speaker.getSessionIds().contains(session.getId());
                    }
                })
                .toList();
    }

    public Observable<List<Speaker>> getSpeakers() {
        return fileObservable("test-speaker.json", LIST_SPEAKER_TYPE);
    }

    public Observable<List<Speaker>> getSpeakersForSession(final Session session) {
        return getSpeakers()
                .flatMap(new Func1<List<Speaker>, Observable<Speaker>>() {
                    @Override
                    public Observable<Speaker> call(List<Speaker> speakers) {
                        return Observable.from(speakers);
                    }
                })
                .filter(new Func1<Speaker, Boolean>() {
                    @Override
                    public Boolean call(Speaker speaker) {
                        return session.getSpeakerIds().contains(speaker.getId());
                    }
                })
                .toSortedList(sortByNameFunction());
    }

    private <T> Observable<T> fileObservable(final String filename, final Type typeOfT) {
        return Observable.create(
                new OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            final T t = loadFile(filename, typeOfT);
                            subscriber.onNext(t);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .compose(new Observable.Transformer<T, T>() {
                    @Override
                    public Observable<T> call(Observable<T> tObservable) {
                        return tObservable.subscribeOn(Schedulers.io()).observeOn(mainThread());
                    }
                })
                .cache();
    }

    private <T> T loadFile(String filename, Type typeOfT) throws IOException {
        final InputStream json = App.getInstance().getAssets().open(filename);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(json, "UTF-8"));
        return gson.get().fromJson(reader, typeOfT);
    }

    private static Func2<Session, Session, Integer> sortByDateFunction() {
        return new Func2<Session, Session, Integer>() {
            @Override
            public Integer call(Session session, Session session2) {
                return session.getBeginning().compareTo(session2.getBeginning());
            }
        };
    }

    private static Func2<Speaker, Speaker, Integer> sortByNameFunction() {
        return new Func2<Speaker, Speaker, Integer>() {
            @Override
            public Integer call(Speaker speaker, Speaker speaker2) {
                return speaker.getName().compareTo(speaker2.getName());
            }
        };
    }
}
