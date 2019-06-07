package org.selfconference;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ProductionAppModule.class)
@Singleton
public interface ApplicationComponent extends MainComponent {
}
