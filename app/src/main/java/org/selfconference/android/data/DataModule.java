package org.selfconference.android.data;

import android.app.Application;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.selfconference.android.data.api.ApiModule;
import org.selfconference.android.data.api.json.EventJsonDeserializer;
import org.selfconference.android.data.api.json.FeedbackJsonSerializer;
import org.selfconference.android.data.api.json.RoomJsonDeserializer;
import org.selfconference.android.data.api.json.SessionJsonDeserializer;
import org.selfconference.android.data.api.json.SpeakerTypeAdapter;
import org.selfconference.android.data.api.json.SponsorJsonDeserializer;
import org.selfconference.android.data.api.json.SponsorLevelJsonDeserializer;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;
import org.selfconference.android.data.pref.SessionPreferences;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
import static org.selfconference.android.BuildConfig.DEBUG;

@Module(
    includes = ApiModule.class,
    complete = false,
    library = true) public final class DataModule {
  @Provides @Singleton Gson gson() {
    return new GsonBuilder() //
        .registerTypeAdapter(Session.class, new SessionJsonDeserializer())
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter())
        .registerTypeAdapter(Sponsor.class, new SponsorJsonDeserializer())
        .registerTypeAdapter(SponsorLevel.class, new SponsorLevelJsonDeserializer())
        .registerTypeAdapter(Room.class, new RoomJsonDeserializer())
        .registerTypeAdapter(Feedback.class, new FeedbackJsonSerializer())
        .registerTypeAdapter(Event.class, new EventJsonDeserializer())
        .create();
  }

  @Provides SessionPreferences sessionPreferences(Application application) {
    return new SessionPreferences(application);
  }

  @Provides @Singleton Picasso picasso(Application application) {
    return new Picasso.Builder(application) //
        .listener((picasso, uri, e) -> Timber.e(e, "Image load failed for URI: %s", uri)) //
        .build();
  }

  @Provides OkHttpClient okHttpClient(Application application) {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(DEBUG ? BODY : NONE);

    long cacheSize = 10 * 10 * 1024; // 10 MiB
    Cache cache = new Cache(application.getCacheDir(), cacheSize);

    return new OkHttpClient.Builder() //
        .addInterceptor(httpLoggingInterceptor) //
        .cache(cache) //
        .build();
  }

  @Provides @Singleton JobManager jobManager(Application application) {
    return new JobManager(new Configuration.Builder(application) //
        .injector(job -> Injector.obtain(application).inject(job)) //
        .build());
  }
}
