package org.selfconference.data;

import org.selfconference.data.api.ApiModule;
import org.selfconference.data.pref.PreferencesModule;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = { ApiModule.class, PreferencesModule.class })
public final class DataModule {

  @Provides @Singleton DataSource dataSource() {
    return new DataSource();
  }

  @Provides @Singleton IntentFactory intentFactory() {
    return IntentFactory.REAL;
  }
}
