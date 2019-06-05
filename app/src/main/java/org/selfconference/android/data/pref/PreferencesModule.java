package org.selfconference.android.data.pref;

import android.app.Application;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module
public class PreferencesModule {

    @Provides @Singleton SharedPreferences sharedPreferences(Application application) {
        return application.getSharedPreferences("self_conf", MODE_PRIVATE);
    }

    @Provides @Singleton RxSharedPreferences rxSharedPreferences(SharedPreferences prefs) {
        return RxSharedPreferences.create(prefs);
    }

    @Provides SessionPreferences sessionPreferences(Application application) {
        return new SessionPreferences(application);
    }
}
