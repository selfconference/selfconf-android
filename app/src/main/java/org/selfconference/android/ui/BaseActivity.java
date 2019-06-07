package org.selfconference.ui;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import org.selfconference.App;
import org.selfconference.R;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends RxAppCompatActivity {

  private Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.context().getApplicationComponent().inject(this);
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
