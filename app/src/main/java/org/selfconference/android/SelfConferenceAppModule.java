package org.selfconference.android;

import android.app.Application;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;

import org.selfconference.android.api.Api;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.SelfConferenceClient;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.feedback.SubmitFeedbackIntentService;
import org.selfconference.android.feedback.Vote;
import org.selfconference.android.feedback.VoteTypeAdapter;
import org.selfconference.android.session.Session;
import org.selfconference.android.session.SessionAdapter;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SessionListFragment;
import org.selfconference.android.session.SessionPreferences;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerListFragment;
import org.selfconference.android.speakers.SpeakerTypeAdapter;
import org.selfconference.android.sponsors.Sponsor;
import org.selfconference.android.sponsors.SponsorAdapter;
import org.selfconference.android.sponsors.SponsorListFragment;
import org.selfconference.android.sponsors.SponsorTypeAdapter;
import org.selfconference.android.utils.PostFromAnyThreadBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

import static org.selfconference.android.BuildConfig.DEBUG;
import static org.selfconference.android.BuildConfig.SELF_CONFERENCE_API_ENDPOINT;
import static retrofit.RestAdapter.LogLevel.BASIC;
import static retrofit.RestAdapter.LogLevel.NONE;

@Module(
        library = true,
        complete = false,
        injects = {
                SelfConferenceApi.class,
                FilterableAdapter.class,
                SponsorAdapter.class,
                SponsorListFragment.class,
                SessionAdapter.class,
                BaseFragment.class,
                SessionContainerFragment.class,
                SpeakerListFragment.class,
                SessionListFragment.class,
                CodeOfConductFragment.class,
                SpeakerAdapter.class,
                BaseActivity.class,
                SessionDetailsActivity.class,
                SubmitFeedbackIntentService.class
        }
)
public class SelfConferenceAppModule {

    private final Application application;

    public SelfConferenceAppModule(Application application) {
        this.application = application;
    }

    @Provides @Singleton Api api() {
        return new SelfConferenceApi();
    }

    @Provides @Singleton Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Session.class, new Session.Deserializer())
                .registerTypeAdapter(Speaker.class, new SpeakerTypeAdapter())
                .registerTypeAdapter(Sponsor.class, new SponsorTypeAdapter())
                .registerTypeAdapter(Vote.class, new VoteTypeAdapter())
                .create();
    }

    @Provides @Singleton SelfConferenceClient selfConferenceClient(Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(SELF_CONFERENCE_API_ENDPOINT)
                .setClient(new OkClient())
                .setConverter(new GsonConverter(gson))
                .setLogLevel(DEBUG ? BASIC : NONE)
                .build()
                .create(SelfConferenceClient.class);
    }

    @Provides @Singleton Picasso picasso() {
        return new Picasso.Builder(application)
                .listener(new Listener() {
                    @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception, "Image load failed for URI: %s", uri);
                    }
                })
                .build();
    }

    @Provides SessionPreferences savedSessionPreferences() {
        return new SessionPreferences(application);
    }

    @Provides @Singleton Bus bus() {
        return new PostFromAnyThreadBus();
    }
}
