package org.selfconference.data;

import android.content.Intent;
import com.f2prateek.rx.preferences2.Preference;
import org.selfconference.ui.ExternalIntentActivity;

public final class DebugIntentFactory implements IntentFactory {
  private final IntentFactory realIntentFactory;
  private final Preference<Boolean> captureIntents;

  public DebugIntentFactory(IntentFactory realIntentFactory, Preference<Boolean> captureIntents) {
    this.realIntentFactory = realIntentFactory;
    this.captureIntents = captureIntents;
  }

  @Override public Intent createUrlIntent(String url) {
    Intent baseIntent = realIntentFactory.createUrlIntent(url);
    if (captureIntents.get()) {
      return ExternalIntentActivity.createIntent(baseIntent);
    }
    return baseIntent;
  }
}
