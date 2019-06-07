package org.selfconference;

import org.selfconference.ui.debug.DebugView;

public interface InternalDebugApplicationComponent extends MainComponent {
    void inject(DebugView debugView);
}
