package org.selfconference.android.ui.debug;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences.Preference;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.CaptureIntents;
import org.selfconference.android.data.Injector;
import org.selfconference.android.ui.decorator.DisplayMetricsDecorator;
import timber.log.Timber;

public final class DebugView extends FrameLayout {

  @Bind(R.id.debug_capture_intents) Switch captureIntentsView;

  @Bind(R.id.debug_device_make) TextView deviceMakeView;
  @Bind(R.id.debug_device_model) TextView deviceModelView;
  @Bind(R.id.debug_device_resolution) TextView deviceResolutionView;
  @Bind(R.id.debug_device_density) TextView deviceDensityView;
  @Bind(R.id.debug_device_release) TextView deviceReleaseView;
  @Bind(R.id.debug_device_api) TextView deviceApiView;

  @Inject @CaptureIntents Preference<Boolean> captureIntents;

  public DebugView(Context context) {
    this(context, null);
  }

  public DebugView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Injector.obtain(context).inject(this);

    LayoutInflater.from(context).inflate(R.layout.debug_view_content, this);
    ButterKnife.bind(this);

    setupMockBehaviorSection();
    setupDeviceSection();
  }

  private void setupMockBehaviorSection() {
    captureIntentsView.setChecked(captureIntents.get());
    captureIntentsView.setOnCheckedChangeListener((compoundButton, isChecked) -> {
      Timber.d("Capture intents set to %s", isChecked);
      captureIntents.set(isChecked);
    });
  }

  private void setupDeviceSection() {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    DisplayMetricsDecorator decorator = DisplayMetricsDecorator.decorate(displayMetrics);

    deviceMakeView.setText(truncateAt(Build.MANUFACTURER, 20));
    deviceModelView.setText(truncateAt(Build.MODEL, 20));
    deviceResolutionView.setText(decorator.resolution());
    deviceDensityView.setText(decorator.density());
    deviceReleaseView.setText(Build.VERSION.RELEASE);
    deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
  }

  private static String truncateAt(String string, int length) {
    return string.length() > length ? string.substring(0, length) : string;
  }
}
