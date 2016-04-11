package org.selfconference.android;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.selfconference.android.data.DebugDataModule;
import org.selfconference.android.ui.DebugUiModule;

@Module(
    addsTo = AppModule.class,
    includes = {
        DebugUiModule.class,
        DebugDataModule.class,
    },
    overrides = true
)
public final class DebugAppModule {
  // Low-tech flag to force certain debug build behaviors when running in an instrumentation test.
  // This value is used in the creation of singletons so it must be set before the graph is created.
  static boolean instrumentationTest = false;

  @Provides @Singleton @IsInstrumentationTest boolean isInstrumentationTest() {
    return instrumentationTest;
  }
}
