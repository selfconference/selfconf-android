package org.selfconference.android;

import android.app.Application;

import org.selfconference.android.data.DataModule;
import org.selfconference.android.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { UiModule.class, DataModule.class })
public class InternalReleaseAppModule {

    private final App app;

    public InternalReleaseAppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    App app() {
        return app;
    }

    @Provides @Singleton
    Application application() {
        return app;
    }
}
