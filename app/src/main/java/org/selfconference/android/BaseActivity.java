package org.selfconference.android;

import android.app.ActivityManager.TaskDescription;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends RxAppCompatActivity {

  private Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (SDK_INT >= LOLLIPOP) {
      setTaskDescription(new TaskDescription(getString(R.string.app_name),
          BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_task),
          getColor(R.color.green)));
    }
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
