package org.selfconference;

public class ComponentFactory {

    public static ApplicationComponent getComponent(App app) {
        return DaggerApplicationComponent.builder()
                .productionAppModule(new ProductionAppModule(app))
                .build();
    }
}
