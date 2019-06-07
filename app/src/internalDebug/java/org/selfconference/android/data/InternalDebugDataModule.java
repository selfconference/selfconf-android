package org.selfconference.data;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import org.selfconference.IsInstrumentationTest;
import org.selfconference.data.api.InternalDebugApiModule;
import org.selfconference.data.pref.PreferencesModule;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = { InternalDebugApiModule.class, PreferencesModule.class })
public final class InternalDebugDataModule {

  private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.

  @Provides @Singleton DataSource dataSource() {
    return new DataSource();
  }

  @Provides @Singleton
  IntentFactory intentFactory(@CaptureIntents Preference<Boolean> captureIntents) {
    return new DebugIntentFactory(IntentFactory.REAL, captureIntents);
  }

  @Provides @Singleton @CaptureIntents
  Preference<Boolean> captureIntents(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
  }

  @Provides @Singleton @ApiEndpoint
  Preference<String> endpoint(RxSharedPreferences preferences) {
    return preferences.getString("debug_endpoint", ApiEndpoints.PRODUCTION.url);
  }

  @Provides @Singleton @IsMockMode
  boolean isMockMode(@ApiEndpoint Preference<String> endpoint,
      @IsInstrumentationTest boolean isInstrumentationTest) {
    // Running in an instrumentation forces mock mode.
    return isInstrumentationTest || ApiEndpoints.isMockMode(endpoint.get());
  }

}
