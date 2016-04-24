package org.selfconference.android.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.selfconference.android.IsInstrumentationTest;
import org.selfconference.android.ui.debug.DebugView;
import org.selfconference.android.ui.debug.DebugViewContainer;

@Module(
    injects = {
        DebugViewContainer.class,
        DebugView.class,
    },
    complete = false,
    library = true,
    overrides = true
)
public class DebugUiModule {
  @Provides @Singleton ViewContainer viewContainer(DebugViewContainer debugViewContainer,
      @IsInstrumentationTest boolean isInstrumentationTest) {
    // Do not add the debug controls for when we are running inside of an instrumentation test.
    return isInstrumentationTest ? ViewContainer.DEFAULT : debugViewContainer;
  }
}
