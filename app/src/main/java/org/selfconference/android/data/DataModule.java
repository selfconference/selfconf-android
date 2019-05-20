package org.selfconference.android.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import org.selfconference.android.data.api.ApiModule;
import org.selfconference.android.data.api.json.InstantAdapter;
import org.selfconference.android.data.api.json.VoteAdapter;
import org.selfconference.android.data.pref.SessionPreferences;
import java.io.File;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;

@Module(
    includes = ApiModule.class,
    complete = false,
    library = true
)
public final class DataModule {
  static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  @Provides @Singleton Moshi moshi() {
    return new Moshi.Builder() //
        .add(MyAdapterFactory.create())
        .add(new InstantAdapter())
        .add(new VoteAdapter())
        .build();
  }

  @Provides @Singleton SharedPreferences sharedPreferences(Application application) {
    return application.getSharedPreferences("self_conf", MODE_PRIVATE);
  }

  @Provides @Singleton RxSharedPreferences rxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs);
  }

  @Provides SessionPreferences sessionPreferences(Application application) {
    return new SessionPreferences(application);
  }

  @Provides @Singleton IntentFactory intentFactory() {
    return IntentFactory.REAL;
  }

  @Provides @Singleton Picasso picasso(Application application, OkHttpClient client) {
    return new Picasso.Builder(application) //
        .listener((picasso, uri, e) -> Timber.e(e, "Image load failed for URI: %s", uri)) //
        .downloader(new OkHttp3Downloader(client)) //
        .build();
  }

  @Provides @Singleton OkHttpClient okHttpClient(Application application) {
    return createOkHttpClient(application).build();
  }

  static OkHttpClient.Builder createOkHttpClient(Application application) {
    File cacheDirectory = new File(application.getCacheDir(), "http");
    Cache cache = new Cache(cacheDirectory, DISK_CACHE_SIZE);

    return new OkHttpClient.Builder() //
        .cache(cache);
  }
}
