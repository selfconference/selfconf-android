package org.selfconference.android.data;

import android.content.Intent;
import org.selfconference.android.ui.ExternalIntentActivity;

public final class DebugIntentFactory implements IntentFactory {
  private final IntentFactory realIntentFactory;

  public DebugIntentFactory(IntentFactory realIntentFactory) {
    this.realIntentFactory = realIntentFactory;
  }

  @Override public Intent createUrlIntent(String url) {
    Intent baseIntent = realIntentFactory.createUrlIntent(url);
    return ExternalIntentActivity.createIntent(baseIntent);
  }
}
