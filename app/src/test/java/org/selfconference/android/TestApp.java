package org.selfconference.android;

public class TestApp extends App {

    @Override
    protected void setUpPushNotifications() {
        // do not set up Parse for tests
    }

    @Override
    protected void setupFabric() {
        // do not initialize Fabric for tests
    }
}
