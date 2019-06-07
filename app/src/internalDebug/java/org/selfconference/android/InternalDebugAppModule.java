package org.selfconference;

import android.app.Application;
import org.selfconference.data.InternalDebugDataModule;
import org.selfconference.ui.InternalDebugUiModule;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = { InternalDebugUiModule.class, InternalDebugDataModule.class})
public final class InternalDebugAppModule {
  private final App app;

  public InternalDebugAppModule(App app) {
    this.app = app;
  }

  // Low-tech flag to force certain debug build behaviors when running in an instrumentation test.
  // This value is used in the creation of singletons so it must be set before the graph is created.
  static boolean instrumentationTest = false;

  @Provides @Singleton @IsInstrumentationTest boolean isInstrumentationTest() {
    return instrumentationTest;
  }

  @Provides @Singleton App app() {
    return app;
  }

  @Provides @Singleton
  Application application() {
    return app;
  }
}
