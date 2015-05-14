package org.selfconference.android;

import android.app.Application;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;

import org.selfconference.android.api.Api;
import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.SelfConferenceClient;
import org.selfconference.android.session.Session;
import org.selfconference.android.speakers.Speaker;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.drawer.DrawerFragment;
import org.selfconference.android.session.SavedSessionPreferences;
import org.selfconference.android.session.SessionAdapter;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.session.SessionListFragment;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerListFragment;
import org.selfconference.android.speakers.SpeakerTypeAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

import static org.selfconference.android.BuildConfig.DEBUG;
import static retrofit.RestAdapter.LogLevel.FULL;
import static retrofit.RestAdapter.LogLevel.NONE;

@Module(
        library = true,
        complete = false,
        injects = {
                SelfConferenceApi.class,
                SessionAdapter.class,
                BaseFragment.class,
                SessionContainerFragment.class,
                SpeakerListFragment.class,
                DrawerFragment.class,
                SessionListFragment.class,
                CodeOfConductFragment.class,
                SpeakerAdapter.class,
                BaseActivity.class,
                SessionDetailsActivity.class
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
                .create();
    }

    @Provides @Singleton SelfConferenceClient selfConferenceClient(Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint("http://selfconference.org/api/events/1")
                .setClient(new OkClient())
                .setConverter(new GsonConverter(gson))
                .setLogLevel(DEBUG ? FULL : NONE)
                .build()
                .create(SelfConferenceClient.class);
    }

    @Provides @Singleton Picasso picasso() {
        return new Picasso.Builder(application)
                .listener(new Listener() {
                    @Override public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception, "Image load failed");
                    }
                })
                .build();
    }

    @Provides SavedSessionPreferences savedSessionPreferences() {
        return new SavedSessionPreferences(application);
    }
}
