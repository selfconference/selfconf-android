package org.selfconference.android.ui;

import android.app.ActivityManager.TaskDescription;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import dagger.ObjectGraph;
import org.selfconference.android.R;
import org.selfconference.android.data.Injector;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends RxAppCompatActivity {

  private ObjectGraph appGraph;
  private Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appGraph = Injector.obtain(getApplication());
    appGraph.inject(this);
    if (SDK_INT >= LOLLIPOP) {
      setTaskDescription(new TaskDescription(getString(R.string.app_name),
          BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_task),
          ContextCompat.getColor(this, R.color.green)));
    }
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return appGraph;
    }
    return super.getSystemService(name);
  }

  protected Toolbar getToolbar() {
    if (toolbar == null) {
      toolbar = (Toolbar) checkNotNull(findViewById(R.id.toolbar));
    }
    return toolbar;
  }

  protected void setStatusBarColor(int color) {
    if (SDK_INT >= LOLLIPOP) {
      getWindow().setStatusBarColor(color);
    }
  }
}
