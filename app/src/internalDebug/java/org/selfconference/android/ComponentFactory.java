package org.selfconference.android;

public class ComponentFactory {

    public static ApplicationComponent getComponent(App app) {
        return DaggerApplicationComponent.builder()
                .internalDebugAppModule(new InternalDebugAppModule(app))
                .build();
    }
}
