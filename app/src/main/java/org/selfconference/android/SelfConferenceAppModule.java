package org.selfconference.android;

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
import org.greenrobot.eventbus.EventBus;
import org.selfconference.android.api.Api;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.SelfConferenceClient;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.api.json.EventJsonDeserializer;
import org.selfconference.android.data.api.json.FeedbackJsonSerializer;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Speaker;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.api.model.SponsorLevel;
import org.selfconference.android.data.jobs.GetSessionJob;
import org.selfconference.android.data.jobs.GetSessionsJob;
import org.selfconference.android.data.jobs.GetSpeakersJob;
import org.selfconference.android.data.jobs.GetSponsorsJob;
import org.selfconference.android.data.jobs.SubmitFeedbackJob;
import org.selfconference.android.data.api.model.Feedback;
import org.selfconference.android.feedback.FeedbackFragment;
import org.selfconference.android.session.RoomJsonDeserializer;
import org.selfconference.android.session.SessionAdapter;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SessionJsonDeserializer;
import org.selfconference.android.session.SessionListFragment;
import org.selfconference.android.session.SessionPreferences;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerListFragment;
import org.selfconference.android.speakers.SpeakerTypeAdapter;
import org.selfconference.android.sponsors.SponsorAdapter;
import org.selfconference.android.sponsors.SponsorJsonDeserializer;
import org.selfconference.android.sponsors.SponsorLevelJsonDeserializer;
import org.selfconference.android.sponsors.SponsorListFragment;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
import static org.selfconference.android.BuildConfig.DEBUG;
import static org.selfconference.android.BuildConfig.SELF_CONFERENCE_API_ENDPOINT;

@Module(
    library = true,
    complete = false,
    injects = {
        SelfConferenceApi.class, //
        FilterableAdapter.class, //
        SponsorAdapter.class, //
        SponsorListFragment.class, //
        SessionAdapter.class, //
        BaseFragment.class, //
        SessionContainerFragment.class, //
        SpeakerListFragment.class, //
        SessionListFragment.class, //
        CodeOfConductFragment.class, //
        FeedbackFragment.class, //
        SpeakerAdapter.class, //
        BaseActivity.class, //
        SessionDetailsActivity.class, //
        ApiJob.class, //
        SubmitFeedbackJob.class, //
        GetSponsorsJob.class, //
        GetSpeakersJob.class, //
        GetSessionJob.class, //
        GetSessionsJob.class, //
    }) //
public final class SelfConferenceAppModule {

  private final Application application;

  public SelfConferenceAppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Api api() {
    return new SelfConferenceApi();
  }

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

  @Provides @Singleton SelfConferenceClient selfConferenceClient(Gson gson,
      OkHttpClient okHttpClient) {
    return new Retrofit.Builder() //
        .baseUrl(SELF_CONFERENCE_API_ENDPOINT)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .validateEagerly(DEBUG)
        .build()
        .create(SelfConferenceClient.class);
  }

  @Provides OkHttpClient okHttpClient() {
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(DEBUG ? BODY : NONE);

    long cacheSize = 10 * 10 * 1024; // 10 MiB
    Cache cache = new Cache(application.getCacheDir(), cacheSize);

    return new OkHttpClient.Builder() //
        .addInterceptor(httpLoggingInterceptor) //
        .cache(cache) //
        .build();
  }

  @Provides @Singleton Picasso picasso() {
    return new Picasso.Builder(application) //
        .listener((picasso, uri, e) -> Timber.e(e, "Image load failed for URI: %s", uri)) //
        .build();
  }

  @Provides SessionPreferences savedSessionPreferences() {
    return new SessionPreferences(application);
  }

  @Provides @Singleton EventBus eventBus() {
    return EventBus.getDefault();
  }

  @Provides @Singleton JobManager jobManager() {
    return new JobManager(new Configuration.Builder(application) //
        .injector(job -> ((App) application).inject(job)).build());
  }
}
