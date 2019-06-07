package org.selfconference.ui;

import org.selfconference.IsInstrumentationTest;
import org.selfconference.ui.debug.DebugViewContainer;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class InternalDebugUiModule {
  @Provides @Singleton ViewContainer viewContainer(DebugViewContainer debugViewContainer,
      @IsInstrumentationTest boolean isInstrumentationTest) {
    // Do not add the debug controls for when we are running inside of an instrumentation test.
    return isInstrumentationTest ? ViewContainer.DEFAULT : debugViewContainer;
  }
}
