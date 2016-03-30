package org.selfconference.android;

import android.app.Application;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.selfconference.android.api.Api;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.SelfConferenceClient;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.feedback.SubmitFeedbackIntentService;
import org.selfconference.android.session.Room;
import org.selfconference.android.session.RoomJsonDeserializer;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionAdapter;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SessionJsonDeserializer;
import org.selfconference.android.session.SessionListFragment;
import org.selfconference.android.session.SessionPreferences;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerListFragment;
import org.selfconference.android.speakers.SpeakerTypeAdapter;
import org.selfconference.android.sponsors.Sponsor;
import org.selfconference.android.sponsors.SponsorAdapter;
import org.selfconference.android.sponsors.SponsorJsonDeserializer;
import org.selfconference.android.sponsors.SponsorLevel;
import org.selfconference.android.sponsors.SponsorLevelJsonDeserializer;
import org.selfconference.android.sponsors.SponsorListFragment;
import org.selfconference.android.utils.PostFromAnyThreadBus;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

import static org.selfconference.android.BuildConfig.DEBUG;
import static org.selfconference.android.BuildConfig.SELF_CONFERENCE_API_ENDPOINT;
import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

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
        SpeakerAdapter.class, //
        BaseActivity.class, //
        SessionDetailsActivity.class, //
        SubmitFeedbackIntentService.class //
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
        .create();
  }

  @Provides @Singleton SelfConferenceClient selfConferenceClient(Gson gson) {
    return new RestAdapter.Builder() //
        .setEndpoint(SELF_CONFERENCE_API_ENDPOINT)
        .setClient(new OkClient())
        .setConverter(new GsonConverter(gson))
        .setLogLevel(DEBUG ? FULL : NONE)
        .build()
        .create(SelfConferenceClient.class);
  }

  @Provides @Singleton Picasso picasso() {
    return new Picasso.Builder(application) //
        .listener((picasso, uri, e) -> Timber.e(e, "Image load failed for URI: %s", uri)) //
        .build();
  }

  @Provides SessionPreferences savedSessionPreferences() {
    return new SessionPreferences(application);
  }

  @Provides @Singleton Bus bus() {
    return new PostFromAnyThreadBus();
  }
}
