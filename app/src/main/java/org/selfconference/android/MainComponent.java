package org.selfconference.android;

import org.selfconference.android.ui.BaseActivity;
import org.selfconference.android.ui.MainActivity;
import org.selfconference.android.ui.session.MyScheduleFragment;
import org.selfconference.android.ui.session.SessionDetailActivity;
import org.selfconference.android.ui.session.SessionListFragment;
import org.selfconference.android.ui.sponsor.SponsorListFragment;

public interface MainComponent {
    App getApp();
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(SessionDetailActivity sessionDetailActivity);
    void inject(SessionListFragment sessionListFragment);
    void inject(SponsorListFragment sponsorListFragment);
    void inject(MyScheduleFragment myScheduleFragment);
}
