package org.selfconference.android.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import org.selfconference.android.R;
import org.selfconference.android.ui.misc.FilterableAdapter;

public abstract class BaseListFragment extends BaseFragment {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.search, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String s) {
        return false;
      }

      @Override public boolean onQueryTextChange(String s) {
        getFilterableAdapter().filter(s);
        return true;
      }
    });
    searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          getFilterableAdapter().reset();
        }
      }
    });
  }

  protected abstract FilterableAdapter getFilterableAdapter();
}
