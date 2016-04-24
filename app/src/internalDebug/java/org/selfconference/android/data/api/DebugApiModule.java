package org.selfconference.android.data.api;

import com.f2prateek.rx.preferences.Preference;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import org.selfconference.android.data.ApiEndpoint;
import org.selfconference.android.data.IsMockMode;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module(
    complete = false,
    library = true,
    overrides = true
)
public final class DebugApiModule {

  @Provides @Singleton HttpUrl httpUrl(@ApiEndpoint Preference<String> apiEndpoint) {
    return HttpUrl.parse(apiEndpoint.get());
  }

  @Provides @Singleton HttpLoggingInterceptor loggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new Logger() {
      @Override public void log(String message) {
        Timber.tag("OkHttp").v(message);
      }
    });
    loggingInterceptor.setLevel(BODY);
    return loggingInterceptor;
  }

  @Provides @Singleton @Named("Api") OkHttpClient apiClient(OkHttpClient client,
      HttpLoggingInterceptor loggingInterceptor) {
    return ApiModule.createApiClient(client)
        .addInterceptor(loggingInterceptor)
        .build();
  }

  @Provides @Singleton NetworkBehavior networkBehavior() {
    return NetworkBehavior.create();
  }

  @Provides @Singleton MockRetrofit mockRetrofit(Retrofit retrofit, NetworkBehavior behavior) {
    return new MockRetrofit.Builder(retrofit) //
        .networkBehavior(behavior) //
        .build();
  }

  @Provides @Singleton RestClient restClient(Retrofit retrofit, @IsMockMode boolean isMockMode,
      MockRestClient mockService) {
    return isMockMode ? mockService : retrofit.create(RestClient.class);
  }
}
