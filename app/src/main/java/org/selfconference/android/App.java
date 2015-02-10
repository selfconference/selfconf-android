package org.selfconference.android;

import android.app.Application;

import dagger.ObjectGraph;
import timber.log.Timber;

public class App extends Application {

    private static App INSTANCE;

    private ObjectGraph objectGraph;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        objectGraph = ObjectGraph.create(new SelfConferenceAppModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
