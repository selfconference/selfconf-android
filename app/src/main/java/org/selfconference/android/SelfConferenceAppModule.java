package org.selfconference.android;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        library = true,
        complete = false,
        injects = {
                SelfConferenceApi.class
        }
)
public class SelfConferenceAppModule {

    private final Application application;

    public SelfConferenceAppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Session.class, new Session.Deserializer())
                .create();
    }
}
