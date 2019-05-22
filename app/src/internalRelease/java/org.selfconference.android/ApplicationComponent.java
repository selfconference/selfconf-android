package org.selfconference.android;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = InternalReleaseAppModule.class)
@Singleton
public interface ApplicationComponent extends MainComponent {
}
