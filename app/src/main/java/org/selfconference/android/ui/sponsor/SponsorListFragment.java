package org.selfconference.android.ui.sponsor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.birbit.android.jobqueue.JobManager;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.selfconference.android.R;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.data.event.GetSponsorsAddEvent;
import org.selfconference.android.data.event.GetSponsorsSuccessEvent;
import org.selfconference.android.data.job.GetSponsorsJob;
import org.selfconference.android.ui.BaseListFragment;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.sponsor.SponsorAdapter.OnSponsorClickListener;
import org.selfconference.android.util.Intents;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class SponsorListFragment extends BaseListFragment implements OnSponsorClickListener {
  public static final String TAG = SponsorListFragment.class.getName();

  @Bind(R.id.sponsor_recycler_view) RecyclerView sponsorRecyclerView;
  @Bind(R.id.sponsor_list_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  @Inject JobManager jobManager;
  @Inject EventBus eventBus;
  @Inject Picasso picasso;

  private SponsorAdapter sponsorAdapter;

  public SponsorListFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

    sponsorAdapter = new SponsorAdapter(picasso);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    sponsorAdapter.setOnSponsorClickListener(this);

    sponsorRecyclerView.setAdapter(sponsorAdapter);
    sponsorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    jobManager.addJobInBackground(new GetSponsorsJob());
  }

  @Override public void onResume() {
    super.onResume();
    eventBus.register(this);
  }

  @Override public void onPause() {
    super.onPause();
    eventBus.unregister(this);
  }

  @Subscribe(threadMode = MAIN) public void onGetSponsorsJobAdded(GetSponsorsAddEvent event) {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Subscribe(threadMode = MAIN)
  public void onGetSponsorsJobSucceeded(GetSponsorsSuccessEvent event) {
    swipeRefreshLayout.setRefreshing(false);
    sponsorAdapter.setData(event.sponsors);
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return sponsorAdapter;
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_sponsor_list;
  }

  @Override public void onSponsorClicked(Sponsor sponsor) {
    Intents.launchUrl(getActivity(), sponsor.link());
  }
}
