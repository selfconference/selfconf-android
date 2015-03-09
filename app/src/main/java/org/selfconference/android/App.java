package org.selfconference.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

import static org.selfconference.android.BuildConfig.DEBUG;

public class App extends Application {

    private static App INSTANCE;

    private ObjectGraph objectGraph;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupFabric();
        INSTANCE = this;
        if (DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        objectGraph = ObjectGraph.create(new SelfConferenceAppModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    protected void setupFabric() {
        Fabric.with(this, new Crashlytics());
    }
}
