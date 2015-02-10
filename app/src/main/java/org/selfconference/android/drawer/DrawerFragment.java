package org.selfconference.android.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.selfconference.android.R;
import org.selfconference.android.schedule.ScheduleFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerFragment extends Fragment implements DrawerAdapter.Callback {
    public static final String TAG = DrawerFragment.class.getName();

    @InjectView(R.id.drawer_recycler_view)
    RecyclerView drawerRecyclerView;

    private DrawerCloser drawerCloser;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            drawerCloser = (DrawerCloser) activity;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Navigation drawer activity must implement DrawerCloser");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawerCloser = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drawerRecyclerView.setAdapter(new DrawerAdapter(this));
        if (savedInstanceState == null) {
            onDrawerItemSelected(DrawerItem.SCHEDULE);
        }
    }

    @Override
    public void onDrawerItemSelected(DrawerItem drawerItem) {
        drawerCloser.closeDrawer();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ScheduleFragment(), ScheduleFragment.TAG)
                .commit();
    }
}
