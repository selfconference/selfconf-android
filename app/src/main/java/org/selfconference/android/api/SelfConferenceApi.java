package org.selfconference.android.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.selfconference.android.App;
import org.selfconference.android.schedule.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class SelfConferenceApi {

    @Inject
    Lazy<Gson> gson;

    public SelfConferenceApi() {
        App.getInstance().inject(this);
    }

    public Observable<List<Session>> loadSchedule() {
        return Observable.create(new Observable.OnSubscribe<List<Session>>() {
            @Override
            public void call(Subscriber<? super List<Session>> subscriber) {
                try {
                    final InputStream json = App.getInstance().getAssets().open("2014-schedule.json");
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(json, "UTF-8"));
                    final List<Session> events = gson.get().fromJson(reader, new TypeToken<List<Session>>() {}.getType());
                    subscriber.onNext(events);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
