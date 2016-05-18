package org.selfconference.android;

import android.app.Application;
import android.support.annotation.NonNull;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.squareup.leakcanary.LeakCanary;
import dagger.ObjectGraph;
import org.selfconference.android.data.Injector;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

import static org.selfconference.android.BuildConfig.DEBUG;

public class App extends Application {

  private static App INSTANCE;

  private ObjectGraph objectGraph;

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

    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return objectGraph;
    }
    return super.getSystemService(name);
  }

  protected void installLeakCanary() {
    LeakCanary.install(this);
  }

  protected void setupFabric() {
    // STOPSHIP: 5/18/16 reinstall Fabric
  }
}
