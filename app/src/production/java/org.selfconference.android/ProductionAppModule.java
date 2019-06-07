package org.selfconference;

import android.app.Application;

import org.selfconference.data.DataModule;
import org.selfconference.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { UiModule.class, DataModule.class })
public final class ProductionAppModule {

  private final App app;

  public ProductionAppModule(App app) {
    this.app = app;
  }

  @Provides @Singleton App app() {
    return app;
  }

  @Provides @Singleton Application application() {
    return app;
  }
}
