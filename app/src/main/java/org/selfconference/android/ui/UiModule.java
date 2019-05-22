package org.selfconference.android.ui;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public final class UiModule {
  @Provides @Singleton ViewContainer viewContainer() {
    return ViewContainer.DEFAULT;
  }
}
