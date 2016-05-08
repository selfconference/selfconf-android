package org.selfconference.android;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.selfconference.android.data.DataModule;
import org.selfconference.android.data.api.ApiJob;
import org.selfconference.android.data.job.GetSessionJob;
import org.selfconference.android.data.job.GetSpeakersJob;
import org.selfconference.android.data.job.GetSponsorsJob;
import org.selfconference.android.data.job.SubmitFeedbackJob;
import org.selfconference.android.ui.BaseActivity;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.ui.UiModule;
import org.selfconference.android.ui.coc.CodeOfConductFragment;
import org.selfconference.android.ui.session.FeedbackFragment;
import org.selfconference.android.ui.session.SessionContainerFragment;
import org.selfconference.android.ui.session.SessionDetailActivity;
import org.selfconference.android.ui.session.SessionListFragment;
import org.selfconference.android.ui.speaker.SpeakerListFragment;
import org.selfconference.android.ui.sponsor.SponsorListFragment;

@Module(
    includes = {
        UiModule.class,
        DataModule.class
    },
    injects = {
        SponsorListFragment.class, //
        BaseFragment.class, //
        SessionContainerFragment.class, //
        SpeakerListFragment.class, //
        SessionListFragment.class, //
        CodeOfConductFragment.class, //
        FeedbackFragment.class, //
        BaseActivity.class, //
        SessionDetailActivity.class, //
        ApiJob.class, //
        SubmitFeedbackJob.class, //
        GetSponsorsJob.class, //
        GetSpeakersJob.class, //
        GetSessionJob.class, //
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
