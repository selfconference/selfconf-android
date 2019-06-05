package org.selfconference.android;

import org.selfconference.android.ui.debug.DebugView;

public interface InternalDebugApplicationComponent extends MainComponent {
    void inject(DebugView debugView);
}
