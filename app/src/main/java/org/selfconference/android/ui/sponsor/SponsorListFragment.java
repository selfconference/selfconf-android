package org.selfconference.android.ui.sponsor;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.FragmentEvent;
import org.selfconference.android.R;
import org.selfconference.android.data.Data;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.DataTransformers;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.IntentFactory;
import org.selfconference.android.data.api.model.Sponsor;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.ui.FragmentCallbacks;
import org.selfconference.android.ui.sponsor.SponsorAdapter.OnSponsorClickListener;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SponsorListFragment extends BaseFragment
    implements OnSponsorClickListener, SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = SponsorListFragment.class.getName();

  @BindView(R.id.sponsor_recycler_view) RecyclerView sponsorRecyclerView;
  @BindView(R.id.sponsor_list_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

  @Inject Picasso picasso;
  @Inject IntentFactory intentFactory;
  @Inject DataSource dataSource;

  private FragmentCallbacks callbacks;
  private SponsorAdapter sponsorAdapter;

  public SponsorListFragment() {
  }

  @Override public void onAttach(Activity activity) {
    try {
      callbacks = (FragmentCallbacks) activity;
    } catch (ClassCastException e) {
      throw new RuntimeException(e);
    }
    super.onAttach(activity);
  }

  @Override public void onDetach() {
    super.onDetach();
    callbacks = null;
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
  }

  @Override public void onStart() {
    super.onStart();

    Observable<Data<List<Sponsor>>> sessionsData = dataSource.sponsors()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindUntilEvent(FragmentEvent.STOP));

    sessionsData.compose(DataTransformers.loading()) //
        .subscribe(__ -> {
          swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        });

    sessionsData.compose(DataTransformers.loaded()) //
        .flatMap(sponsors -> Observable.from(sponsors).toSortedList()) //
        .subscribe(sponsors -> {
          sponsorAdapter.setSponsors(sponsors);
        });

    sessionsData.compose(DataTransformers.error()) //
        .subscribe(throwable -> {
          Timber.e(throwable, "Something happened here");
        });
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_sponsor_list;
  }

  @Override public void onSponsorClicked(Sponsor sponsor) {
    startActivity(intentFactory.createUrlIntent(sponsor.link()));
  }

  @Override public void onRefresh() {
    if (callbacks != null) {
      callbacks.onRequestSponsors();
    }
  }
}
