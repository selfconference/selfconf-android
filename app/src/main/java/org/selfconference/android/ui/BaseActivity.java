package org.selfconference.android.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import org.selfconference.android.R;
import org.selfconference.android.data.Injector;
import dagger.ObjectGraph;
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
