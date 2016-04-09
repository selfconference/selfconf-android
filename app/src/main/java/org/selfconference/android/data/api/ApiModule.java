package org.selfconference.android.data.api;

import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.selfconference.android.BuildConfig.DEBUG;

@Module(
    complete = false,
    library = true
)
public final class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse("http://selfconf-dev.herokuapp.com/api/");

  @Provides @Singleton HttpUrl baseUrl() {
    return PRODUCTION_API_URL;
  }

  @Provides @Singleton Api api(RestClient restClient) {
    return new RestApi(restClient);
  }

  @Provides @Singleton Retrofit retrofit(HttpUrl baseUrl, Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder() //
        .baseUrl(baseUrl) //
        .addConverterFactory(GsonConverterFactory.create(gson)) //
        .client(okHttpClient) //
        .validateEagerly(DEBUG) //
        .build();
  }

  @Provides @Singleton RestClient restClient(Retrofit retrofit) {
    return retrofit.create(RestClient.class);
  }
}
