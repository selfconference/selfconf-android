package org.selfconference.android.sponsors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.InjectView;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.BaseListFragment;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import org.selfconference.android.api.ApiRequestSubscriber;
import org.selfconference.android.sponsors.SponsorAdapter.OnSponsorClickListener;
import org.selfconference.android.utils.Intents;
import org.selfconference.android.utils.rx.Transformers;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

import static rx.android.app.AppObservable.bindFragment;

public class SponsorListFragment extends BaseListFragment implements OnSponsorClickListener {
  public static final String TAG = SponsorListFragment.class.getName();

  @InjectView(R.id.sponsor_recycler_view) RecyclerView sponsorRecyclerView;
  @InjectView(R.id.sponsor_list_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  @Inject Api api;

  private final SponsorAdapter sponsorAdapter = new SponsorAdapter();

  public SponsorListFragment() {
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    sponsorAdapter.setOnSponsorClickListener(this);

    sponsorRecyclerView.setAdapter(sponsorAdapter);
    sponsorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    final Observable<List<Sponsor>> sponsorsObservable = bindFragment(this, api.getSponsors()) //
        .map(new Func1<List<Sponsor>, List<Sponsor>>() {
          @Override public List<Sponsor> call(List<Sponsor> sponsors) {
            Collections.sort(sponsors, new SponsorComparator());
            return sponsors;
          }
        }) //
        .compose(Transformers.<List<Sponsor>>setRefreshing(swipeRefreshLayout));

    addSubscription(sponsorsObservable.subscribe(sponsorsSubscriber));
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return sponsorAdapter;
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_sponsor_list;
  }

  @Override public void onSponsorClicked(Sponsor sponsor) {
    Intents.launchUrl(getActivity(), sponsor.getLink());
  }

  private final Subscriber<List<Sponsor>> sponsorsSubscriber =
      new ApiRequestSubscriber<List<Sponsor>>() {

        @Override public void onError(Throwable e) {
          super.onError(e);
          Timber.e(e, "Failed to load sponsors");
        }

        @Override public void onNext(List<Sponsor> sponsors) {
          sponsorAdapter.setData(sponsors);
        }
      };
}
