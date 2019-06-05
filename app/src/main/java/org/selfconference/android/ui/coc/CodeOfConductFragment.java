package org.selfconference.android.ui.coc;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.selfconference.android.R;
import org.selfconference.android.ui.BaseFragment;
import butterknife.BindView;

public final class CodeOfConductFragment extends BaseFragment {
  public static final String TAG = CodeOfConductFragment.class.getName();

  @BindView(R.id.code_of_conduct_recycler_view) RecyclerView recyclerView;

  @Override protected int layoutResId() {
    return R.layout.fragment_code_of_conduct;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(false);
    getActivity().invalidateOptionsMenu();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    recyclerView.setAdapter(new CodeOfConductAdapter());
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
  }
}
