package org.selfconference.android.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.squareup.picasso.Picasso;
import org.selfconference.android.IsInstrumentationTest;
import org.selfconference.android.data.api.DebugApiModule;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

@Module(
    includes = DebugApiModule.class,
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

  @Provides @Singleton OkHttpClient okHttpClient(Application application) {
    return DataModule.createOkHttpClient(application).build();
  }

  @Provides @Singleton Picasso picasso(NetworkBehavior behavior, @IsMockMode boolean isMockMode,
      Application application) {
    Picasso.Builder builder = new Picasso.Builder(application);
    if (isMockMode) {
      builder.addRequestHandler(new MockRequestHandler(behavior, application.getAssets()));
    }
    builder.listener(new Picasso.Listener() {
      @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
        Timber.e(exception, "Error while loading image %s", uri);
      }
    });
    return builder.build();
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
