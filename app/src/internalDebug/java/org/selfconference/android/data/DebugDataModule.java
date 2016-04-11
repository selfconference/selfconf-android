package org.selfconference.android.data;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    complete = false,
    library = true,
    overrides = true
)
public final class DebugDataModule {
  @Provides @Singleton IntentFactory intentFactory() {
    return new DebugIntentFactory(IntentFactory.REAL);
  }
}
