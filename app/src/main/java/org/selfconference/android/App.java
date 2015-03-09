package org.selfconference.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

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
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_id));
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Timber.d("Successfully subscribed to Parse Push");
                } else {
                    Timber.e(e, "Error subscribing to Parse Push");
                }
            }
        });

        objectGraph = ObjectGraph.create(new SelfConferenceAppModule(this));
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    protected void setupFabric() {
        Fabric.with(this, new Crashlytics());
    }
}
