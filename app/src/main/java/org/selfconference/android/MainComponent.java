package org.selfconference;

import org.selfconference.ui.BaseActivity;
import org.selfconference.ui.MainActivity;
import org.selfconference.ui.session.MyScheduleFragment;
import org.selfconference.ui.session.SessionDetailActivity;
import org.selfconference.ui.session.SessionListFragment;
import org.selfconference.ui.sponsor.SponsorListFragment;

public interface MainComponent {
    App getApp();
    void inject(BaseActivity baseActivity);
    void inject(MainActivity mainActivity);
    void inject(SessionDetailActivity sessionDetailActivity);
    void inject(SessionListFragment sessionListFragment);
    void inject(SponsorListFragment sponsorListFragment);
    void inject(MyScheduleFragment myScheduleFragment);
}
