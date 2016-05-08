package org.selfconference.android.ui.sponsor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.IntentFactory;
import org.selfconference.android.data.api.RestClient;
import org.selfconference.android.data.api.Results;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.ui.BaseListFragment;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.sponsor.SponsorAdapter.OnSponsorClickListener;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class SponsorListFragment extends BaseListFragment
    implements OnSponsorClickListener, OnRefreshListener {
  public static final String TAG = SponsorListFragment.class.getName();

  @BindView(R.id.sponsor_recycler_view) RecyclerView sponsorRecyclerView;
  @BindView(R.id.sponsor_list_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  @Inject RestClient restClient;
  @Inject Picasso picasso;
  @Inject IntentFactory intentFactory;

  private final PublishSubject<Void> sponsorsSubject = PublishSubject.create();

  private SponsorAdapter sponsorAdapter;

  public SponsorListFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);

    sponsorAdapter = new SponsorAdapter(picasso);
    sponsorAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        swipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    sponsorAdapter.setOnSponsorClickListener(this);

    sponsorRecyclerView.setAdapter(sponsorAdapter);
    sponsorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    Observable<Result<List<Sponsor>>> result =
        sponsorsSubject.flatMap(__ -> restClient.getSponsors().subscribeOn(Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .compose(bindToLifecycle())
            .share();

    result.filter(Results.isSuccessful())
        .map(Results.responseBody())
        .flatMap(sponsors -> Observable.from(sponsors).toSortedList())
        .subscribe(sponsors -> {
          sponsorAdapter.setData(sponsors);
        });

    onRefresh();
  }

  @Override protected FilterableAdapter getFilterableAdapter() {
    return sponsorAdapter;
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_sponsor_list;
  }

  @Override public void onSponsorClicked(Sponsor sponsor) {
    startActivity(intentFactory.createUrlIntent(sponsor.link()));
  }

  @Override public void onRefresh() {
    getView().post(() -> {
      swipeRefreshLayout.setRefreshing(true);
      sponsorsSubject.onNext(null);
    });
  }
}
