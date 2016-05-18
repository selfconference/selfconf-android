package org.selfconference.android.ui.event;

import android.os.Bundle;
import com.trello.rxlifecycle.FragmentEvent;
import javax.inject.Inject;
import org.selfconference.android.R;
import org.selfconference.android.data.Data;
import org.selfconference.android.data.DataSource;
import org.selfconference.android.data.DataTransformers;
import org.selfconference.android.data.Injector;
import org.selfconference.android.data.api.model.Event;
import org.selfconference.android.ui.BaseFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class AboutFragment extends BaseFragment {
  public static final String TAG = AboutFragment.class.getName();

  @Inject DataSource dataSource;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Injector.obtain(getActivity().getApplicationContext()).inject(this);
  }

  @Override public void onStart() {
    super.onStart();

    Observable<Data<Event>> eventData = dataSource.event()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindUntilEvent(FragmentEvent.STOP));

    eventData.compose(DataTransformers.loaded()) //
        .subscribe(event -> Timber.d(event.toString()));
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_about;
  }
}
