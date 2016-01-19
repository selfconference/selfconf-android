package org.selfconference.android;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

import static org.selfconference.android.BuildConfig.DEBUG;

public class App extends Application {

  private static App INSTANCE;

  private ObjectGraph objectGraph;

  public static App getInstance() {
    return INSTANCE;
  }

  @Override public void onCreate() {
    super.onCreate();
    installLeakCanary();
    setupFabric();
    INSTANCE = this;
    if (DEBUG) {
      Timber.plant(new DebugTree());
    }
    objectGraph = ObjectGraph.create(new SelfConferenceAppModule(this));
  }

  public void inject(Object object) {
    objectGraph.inject(object);
  }

  protected void installLeakCanary() {
    LeakCanary.install(this);
  }

  protected void setupFabric() {
    Fabric.with(this, new Crashlytics());
  }
}
