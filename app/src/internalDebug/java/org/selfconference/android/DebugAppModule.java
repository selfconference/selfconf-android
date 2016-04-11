package org.selfconference.android;

import dagger.Module;
import org.selfconference.android.data.DebugDataModule;

@Module(
    addsTo = AppModule.class,
    includes = {
        DebugDataModule.class,
    },
    overrides = true
)
public final class DebugAppModule {
}
