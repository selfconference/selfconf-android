package org.selfconference.android.ui.coc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import org.selfconference.android.ui.BaseFragment;
import org.selfconference.android.R;

public final class CodeOfConductFragment extends BaseFragment {
  public static final String TAG = CodeOfConductFragment.class.getName();

  @Bind(R.id.code_of_conduct_recycler_view) RecyclerView recyclerView;

  @Override protected int layoutResId() {
    return R.layout.fragment_code_of_conduct;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(false);
    getActivity().supportInvalidateOptionsMenu();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView.setAdapter(new CodeOfConductAdapter());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }
}
