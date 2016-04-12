package org.selfconference.android.ui.debug;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Switch;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences.Preference;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.CaptureIntents;
import org.selfconference.android.data.Injector;
import timber.log.Timber;

public final class DebugView extends FrameLayout {

  @Bind(R.id.debug_capture_intents) Switch captureIntentsView;

  @Inject @CaptureIntents Preference<Boolean> captureIntents;

  public DebugView(Context context) {
    this(context, null);
  }

  public DebugView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Injector.obtain(context).inject(this);

    LayoutInflater.from(context).inflate(R.layout.debug_view_content, this);
    ButterKnife.bind(this);

    captureIntentsView.setChecked(captureIntents.get());
    captureIntentsView.setOnCheckedChangeListener((compoundButton, isChecked) -> {
      Timber.d("Capture intents set to %s", isChecked);
      captureIntents.set(isChecked);
    });
  }
}
