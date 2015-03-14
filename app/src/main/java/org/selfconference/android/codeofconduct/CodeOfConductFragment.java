package org.selfconference.android.codeofconduct;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;

import butterknife.InjectView;

public class CodeOfConductFragment extends BaseFragment {
    public static final String TAG = CodeOfConductFragment.class.getName();

    @InjectView(R.id.code_of_conduct_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int layoutResId() {
        return R.layout.fragment_code_of_conduct;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setAdapter(new CodeOfConductAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
