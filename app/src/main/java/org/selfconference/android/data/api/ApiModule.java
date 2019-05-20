package org.selfconference.android.data.api;

import com.squareup.moshi.Moshi;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static org.selfconference.android.BuildConfig.DEBUG;

@Module(
    complete = false,
    library = true
)
public final class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse("http://selfconference.org/api/");

  @Provides @Singleton HttpUrl baseUrl() {
    return PRODUCTION_API_URL;
  }

  @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client) {
    return createApiClient(client).build();
  }

  @Provides @Singleton
  Retrofit retrofit(HttpUrl baseUrl, Moshi moshi, @Named("Api") OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .validateEagerly(DEBUG)
        .build();
  }

  @Provides @Singleton RestClient restClient(Retrofit retrofit) {
    return retrofit.create(RestClient.class);
  }

  static OkHttpClient.Builder createApiClient(OkHttpClient client) {
    return client.newBuilder();
  }
}
