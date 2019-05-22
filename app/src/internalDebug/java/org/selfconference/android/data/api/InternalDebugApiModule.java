package org.selfconference.android.data.api;

import android.app.Application;
import android.net.Uri;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import org.selfconference.android.data.ApiEndpoint;
import org.selfconference.android.data.IsMockMode;
import org.selfconference.android.data.MockRequestHandler;
import org.selfconference.android.data.PicassoDebugging;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module
public final class InternalDebugApiModule {

  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed

  @Provides @Singleton HttpUrl httpUrl(@ApiEndpoint Preference<String> apiEndpoint) {
    return HttpUrl.parse(apiEndpoint.get());
  }

  @Provides @Singleton HttpLoggingInterceptor loggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(BODY);
    return loggingInterceptor;
  }

  @Provides @Singleton MockRetrofit mockRetrofit(Retrofit retrofit, NetworkBehavior behavior) {
    return new MockRetrofit.Builder(retrofit)
            .networkBehavior(behavior)
            .build();
  }

  @Provides @Singleton Moshi moshi() {
    return ApiModule.createMoshi();
  }

  @Provides @Singleton OkHttpClient apiClient(Application application,
      HttpLoggingInterceptor loggingInterceptor) {
    return ApiModule.createOkHttpClient(application)
        .addInterceptor(loggingInterceptor)
        .build();
  }

  @Provides @Singleton NetworkBehavior networkBehavior() {
    return NetworkBehavior.create();
  }

  @Provides @Singleton
  Picasso picasso(NetworkBehavior behavior, @IsMockMode boolean isMockMode,
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

  @Provides @Singleton @PicassoDebugging
  Preference<Boolean> picassoDebugging(RxSharedPreferences preferences) {
    return preferences.getBoolean("debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }

  @Provides @Singleton Retrofit retrofit(HttpUrl baseUrl, Moshi moshi, OkHttpClient client) {
    return ApiModule.createRetrofit(baseUrl, moshi, client);
  }

  @Provides @Singleton RestClient restClient(Retrofit retrofit, @IsMockMode boolean isMockMode,
      MockRestClient mockService) {
    return isMockMode ? mockService : ApiModule.createRestClient(retrofit);
  }
}
