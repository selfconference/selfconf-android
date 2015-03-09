package org.selfconference.android;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

public class CustomTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link org.robolectric.annotation.Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws org.junit.runners.model.InitializationError if junit says so
     */
    public CustomTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}