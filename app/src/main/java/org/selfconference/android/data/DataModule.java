package org.selfconference.android.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.selfconference.android.data.api.ApiModule;
import org.selfconference.android.data.api.json.EventJsonDeserializer;
import org.selfconference.android.data.api.json.FeedbackJsonSerializer;
import org.selfconference.android.data.api.json.OrganizerJsonDeserializer;
import org.selfconference.android.data.api.json.RoomJsonDeserializer;
import org.selfconference.android.data.api.json.SessionJsonDeserializer;
import org.selfconference.android.data.api.json.SpeakerTypeAdapter;
import org.selfconference.android.data.api.json.SponsorJsonDeserializer;
import org.selfconference.android.data.api.json.SponsorLevelJsonDeserializer;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.data.api.model.Organizer;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;
import org.selfconference.android.data.pref.SessionPreferences;
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

  @Provides @Singleton Gson gson() {
    return new GsonBuilder() //
        .registerTypeAdapter(Session.class, new SessionJsonDeserializer())
        .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter())
        .registerTypeAdapter(Sponsor.class, new SponsorJsonDeserializer())
        .registerTypeAdapter(SponsorLevel.class, new SponsorLevelJsonDeserializer())
        .registerTypeAdapter(Room.class, new RoomJsonDeserializer())
        .registerTypeAdapter(Feedback.class, new FeedbackJsonSerializer())
        .registerTypeAdapter(Event.class, new EventJsonDeserializer())
        .registerTypeAdapter(Organizer.class, new OrganizerJsonDeserializer())
        .create();
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

  @Provides @Singleton Picasso picasso(Application application) {
    return new Picasso.Builder(application) //
        .listener(new Picasso.Listener() {
          @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
            Timber.e(e, "Image load failed for URI: %s", uri);
          }
        }) //
        .build();
  }

  @Provides @Singleton OkHttpClient okHttpClient(Application application) {
    return createOkHttpClient(application).build();
  }

  @Provides @Singleton JobManager jobManager(final Application application) {
    return new JobManager(new Configuration.Builder(application) //
        .injector(new DependencyInjector() {
          @Override public void inject(Job job) {
            Injector.obtain(application).inject(job);
          }
        }) //
        .build());
  }

  static OkHttpClient.Builder createOkHttpClient(Application application) {
    File cacheDirectory = new File(application.getCacheDir(), "http");
    Cache cache = new Cache(cacheDirectory, DISK_CACHE_SIZE);

    return new OkHttpClient.Builder() //
        .cache(cache);
  }
}
