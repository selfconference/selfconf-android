package org.selfconference.android;

import android.app.Application;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.selfconference.android.api.SelfConferenceApi;
import org.selfconference.android.api.Session;
import org.selfconference.android.drawer.DrawerFragment;
import org.selfconference.android.session.SessionListFragment;
import org.selfconference.android.session.SavedSessionPreferences;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.session.SessionAdapter;
import org.selfconference.android.session.SessionDetailsActivity;
import org.selfconference.android.speakers.SpeakerAdapter;
import org.selfconference.android.speakers.SpeakerListFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

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

    @Provides
    @Singleton
    SelfConferenceApi selfConferenceApi() {
        return new SelfConferenceApi();
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Session.class, new Session.Deserializer())
                .create();
    }

    @Provides
    @Singleton
    Picasso picasso() {
        return new Picasso.Builder(application)
                .loggingEnabled(BuildConfig.DEBUG)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception, "Image load failed");
                    }
                })
                .build();
    }

    @Provides
    SavedSessionPreferences savedSessionPreferences() {
        return new SavedSessionPreferences(application);
    }
}
