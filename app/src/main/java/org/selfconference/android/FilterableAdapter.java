package org.selfconference.android;

import android.support.v7.widget.RecyclerView;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

import static com.google.common.collect.Lists.newArrayList;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public abstract class FilterableAdapter<T, VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {

  private final List<T> data = newArrayList();
  private final List<T> filteredData = newArrayList();

  public FilterableAdapter() {
    App.getInstance().inject(this);
  }

  public void filter(String query) {
    filteredData.clear();
    dataObservable().filter(filterPredicate(query)).subscribe(new FilteredDataSubscriber<>(this));
  }

  public void reset() {
    filteredData.clear();
    filteredData.addAll(data);
    notifyDataSetChanged();
  }

  public void setData(List<T> data) {
    this.data.clear();
    this.data.addAll(data);
    reset();
    notifyDataSetChanged();
  }

  public List<T> getFilteredData() {
    return filteredData;
  }

  protected Observable<T> dataObservable() {
    return Observable.from(getData()).subscribeOn(io()).observeOn(mainThread());
  }

  protected List<T> getData() {
    return data;
  }

  @Override public int getItemCount() {
    return getFilteredData().size();
  }

  protected abstract Func1<T, Boolean> filterPredicate(String query);
}
