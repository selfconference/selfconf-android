package org.selfconference;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = InternalDebugAppModule.class)
@Singleton
public interface ApplicationComponent extends InternalDebugApplicationComponent {
}
