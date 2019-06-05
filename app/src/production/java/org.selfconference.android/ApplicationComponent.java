package org.selfconference.android;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ProductionAppModule.class)
@Singleton
public interface ApplicationComponent extends MainComponent {
}
