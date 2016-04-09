package org.selfconference.android.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

@Module(
    injects = {
        MainActivity.class,
    },
    complete = false,
    library = true
)
public final class UiModule {
  @Provides @Singleton EventBus eventBus() {
    return EventBus.getDefault();
  }
}
