package org.selfconference.android;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.selfconference.android.data.DataModule;
import org.selfconference.android.ui.BaseActivity;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.ui.UiModule;
import org.selfconference.android.ui.coc.CodeOfConductFragment;
import org.selfconference.android.ui.event.AboutFragment;
import org.selfconference.android.ui.session.FeedbackFragment;
import org.selfconference.android.ui.session.MyScheduleFragment;
import org.selfconference.android.ui.session.SessionContainerFragment;
import org.selfconference.android.ui.session.SessionDetailActivity;
import org.selfconference.android.ui.session.SessionListFragment;
import org.selfconference.android.ui.sponsor.SponsorListFragment;

@Module(
    includes = {
        UiModule.class,
        DataModule.class
    },
    injects = {
        SponsorListFragment.class, //
        BaseFragment.class, //
        AboutFragment.class, //
        SessionContainerFragment.class, //
        SessionListFragment.class, //
        MyScheduleFragment.class, //
        CodeOfConductFragment.class, //
        FeedbackFragment.class, //
        BaseActivity.class, //
        SessionDetailActivity.class, //
        App.class, //
    }
)
public final class AppModule {

  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides @Singleton Application application() {
    return app;
  }
}
