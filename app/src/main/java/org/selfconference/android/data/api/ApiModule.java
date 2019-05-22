package org.selfconference.android.data.api;

import android.app.Application;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import org.selfconference.android.data.MyAdapterFactory;
import org.selfconference.android.data.api.json.InstantAdapter;
import org.selfconference.android.data.api.json.VoteAdapter;
import java.io.File;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import timber.log.Timber;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
import static org.selfconference.android.BuildConfig.DEBUG;

@Module
public final class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse("https://selfconference.org/api/");

  @Provides @Singleton HttpUrl baseUrl() {
    return PRODUCTION_API_URL;
  }

  public static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  @Provides @Singleton
  Moshi moshi() {
    return createMoshi();
  }

  @Provides @Singleton OkHttpClient okHttpClient(Application application) {
    return createOkHttpClient(application).build();
  }

  @Provides @Singleton RestClient provideRestClient(Retrofit retrofit) {
    return createRestClient(retrofit);
  }

  @Provides @Singleton
  Retrofit retrofit(HttpUrl baseUrl, Moshi moshi, OkHttpClient okHttpClient) {
    return createRetrofit(baseUrl, moshi, okHttpClient);
  }

  @Provides @Singleton
  Picasso picasso(Application application, OkHttpClient client) {
    return createPicasso(application, client);
  }

  static Moshi createMoshi() {
    return new Moshi.Builder()
            .add(MyAdapterFactory.create())
            .add(new InstantAdapter())
            .add(new VoteAdapter())
            .build();
  }

  static OkHttpClient.Builder createOkHttpClient(Application application) {
    File cacheDirectory = new File(application.getCacheDir(), "http");
    Cache cache = new Cache(cacheDirectory, DISK_CACHE_SIZE);

    return new OkHttpClient.Builder()
            .cache(cache);
  }

  static Picasso createPicasso(Application application, OkHttpClient client) {
    return new Picasso.Builder(application)
            .listener((picasso, uri, e) -> Timber.e(e, "Image load failed for URI: %s", uri))
            .downloader(new OkHttp3Downloader(client))
            .build();
  }

  static RestClient createRestClient(Retrofit retrofit) {
    return retrofit.create(RestClient.class);
  }

  static Retrofit createRetrofit(HttpUrl baseUrl, Moshi moshi, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .validateEagerly(DEBUG)
            .build();
  }

  private static OkHttpClient.Builder createApiClient(OkHttpClient client) {
    return client.newBuilder();
  }
}
