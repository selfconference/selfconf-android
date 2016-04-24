package org.selfconference.android.ui.debug;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.ApiEndpoint;
import org.selfconference.android.data.ApiEndpoints;
import org.selfconference.android.data.CaptureIntents;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.PicassoDebugging;
import org.selfconference.android.ui.decorator.DisplayMetricsDecorator;
import org.selfconference.android.ui.decorator.StatsSnapshotDecorator;
import org.selfconference.android.ui.misc.EnumAdapter;
import timber.log.Timber;

public final class DebugView extends FrameLayout {

  @Bind(R.id.debug_network_endpoint) Spinner endpointView;

  @Bind(R.id.debug_capture_intents) Switch captureIntentsView;

  @Bind(R.id.debug_device_make) TextView deviceMakeView;
  @Bind(R.id.debug_device_model) TextView deviceModelView;
  @Bind(R.id.debug_device_resolution) TextView deviceResolutionView;
  @Bind(R.id.debug_device_density) TextView deviceDensityView;
  @Bind(R.id.debug_device_release) TextView deviceReleaseView;
  @Bind(R.id.debug_device_api) TextView deviceApiView;

  @Bind(R.id.debug_picasso_indicators) Switch picassoIndicatorView;
  @Bind(R.id.debug_picasso_cache_size) TextView picassoCacheSizeView;
  @Bind(R.id.debug_picasso_cache_hit) TextView picassoCacheHitView;
  @Bind(R.id.debug_picasso_cache_miss) TextView picassoCacheMissView;
  @Bind(R.id.debug_picasso_decoded) TextView picassoDecodedView;
  @Bind(R.id.debug_picasso_decoded_total) TextView picassoDecodedTotalView;
  @Bind(R.id.debug_picasso_decoded_avg) TextView picassoDecodedAvgView;
  @Bind(R.id.debug_picasso_transformed) TextView picassoTransformedView;
  @Bind(R.id.debug_picasso_transformed_total) TextView picassoTransformedTotalView;
  @Bind(R.id.debug_picasso_transformed_avg) TextView picassoTransformedAvgView;

  @Inject Picasso picasso;
  @Inject @ApiEndpoint Preference<String> networkEndpoint;
  @Inject @CaptureIntents Preference<Boolean> captureIntents;
  @Inject @PicassoDebugging Preference<Boolean> picassoDebugging;

  public DebugView(Context context) {
    this(context, null);
  }

  public DebugView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Injector.obtain(context).inject(this);

    LayoutInflater.from(context).inflate(R.layout.debug_view_content, this);
    ButterKnife.bind(this);

    setupNetworkSection();
    setupMockBehaviorSection();
    setupDeviceSection();
    setupPicassoSection();
  }

  private void setupNetworkSection() {
    final ApiEndpoints currentEndpoint = ApiEndpoints.from(networkEndpoint.get());
    final EnumAdapter<ApiEndpoints> endpointAdapter =
        new EnumAdapter<>(getContext(), ApiEndpoints.class);
    endpointView.setAdapter(endpointAdapter);
    endpointView.setSelection(currentEndpoint.ordinal());

    endpointView.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ApiEndpoints selected = endpointAdapter.getItem(position);
        if (selected != currentEndpoint) {
          setEndpointAndRelaunch(selected.url);
        }
      }
    });
  }

  private void setupMockBehaviorSection() {
    captureIntentsView.setChecked(captureIntents.get());
    captureIntentsView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Timber.d("Capture intents set to %s", isChecked);
        captureIntents.set(isChecked);
      }
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

  private void setupPicassoSection() {
    boolean picassoDebuggingValue = picassoDebugging.get();
    picasso.setIndicatorsEnabled(picassoDebuggingValue);
    picassoIndicatorView.setChecked(picassoDebuggingValue);
    picassoIndicatorView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        Timber.d("Setting Picasso debugging to " + isChecked);
        picasso.setIndicatorsEnabled(isChecked);
        picassoDebugging.set(isChecked);
      }
    });

    refreshPicassoStats();
  }

  private void refreshPicassoStats() {
    StatsSnapshot snapshot = picasso.getSnapshot();
    StatsSnapshotDecorator decorator = StatsSnapshotDecorator.decorate(snapshot);

    picassoCacheSizeView.setText(decorator.cacheSize());
    picassoCacheHitView.setText(decorator.cacheHits());
    picassoCacheMissView.setText(decorator.cacheMisses());
    picassoDecodedView.setText(decorator.originalBitmapCount());
    picassoDecodedTotalView.setText(decorator.totalOriginalBitmapSize());
    picassoDecodedAvgView.setText(decorator.averageOriginalBitmapSize());
    picassoTransformedView.setText(decorator.transformedBitmapCount());
    picassoTransformedTotalView.setText(decorator.totalTransformedBitmapSize());
    picassoTransformedAvgView.setText(decorator.averageTransformedBitmapSize());
  }

  private void setEndpointAndRelaunch(String endpoint) {
    Timber.d("Setting network endpoint to %s", endpoint);
    networkEndpoint.set(endpoint);

    ProcessPhoenix.triggerRebirth(getContext());
  }

  private static String truncateAt(String string, int length) {
    return string.length() > length ? string.substring(0, length) : string;
  }

  static abstract class SimpleOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override public void onNothingSelected(AdapterView<?> parent) {
      // No-op
    }
  }
}
