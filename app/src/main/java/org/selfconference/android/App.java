package org.selfconference;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import org.selfconference.util.CrashlyticsTree;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

import static org.selfconference.BuildConfig.DEBUG;

public class App extends Application {

  private static App INSTANCE;

  private ApplicationComponent applicationComponent;

  public static App context() {
    return INSTANCE;
  }

  @Override public void onCreate() {
    super.onCreate();

    INSTANCE = this;

    installLeakCanary();
    setupFabric();

    AndroidThreeTen.init(this);

    if (DEBUG) {
      Timber.plant(new DebugTree());
    }

    applicationComponent = ComponentFactory.getComponent(this);
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

  protected void installLeakCanary() {
    LeakCanary.install(this);
  }

  protected void setupFabric() {
    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
      Timber.plant(new CrashlyticsTree());
    }
  }
}
