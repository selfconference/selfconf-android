package org.selfconference.android.data;

import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    complete = false,
    library = true,
    overrides = true
)
public final class DebugDataModule {

  private static final boolean DEFAULT_CAPTURE_INTENTS = true; // Capture external intents.
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed

  @Provides @Singleton RxSharedPreferences rxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs);
  }

  @Provides @Singleton
  IntentFactory intentFactory(@CaptureIntents Preference<Boolean> captureIntents) {
    return new DebugIntentFactory(IntentFactory.REAL, captureIntents);
  }

  @Provides @Singleton @CaptureIntents
  Preference<Boolean> captureIntents(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
  }

  @Provides @Singleton @PicassoDebugging
  Preference<Boolean> picassoDebugging(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }

}
