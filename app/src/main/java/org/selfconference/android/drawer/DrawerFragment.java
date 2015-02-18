package org.selfconference.android.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.schedule.ScheduleFragment;
import org.selfconference.android.speakers.SpeakerFragment;

import butterknife.InjectView;

public class DrawerFragment extends BaseFragment implements DrawerAdapter.OnDrawerItemClickListener {
    public static final String TAG = DrawerFragment.class.getName();

    @InjectView(R.id.drawer_recycler_view)
    RecyclerView drawerRecyclerView;

    private DrawerCloser drawerCloser;
    private DrawerAdapter adapter = new DrawerAdapter();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter.setOnDrawerItemClickListener(this);

        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drawerRecyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            onDrawerItemClick(DrawerItem.SCHEDULE);
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.fragment_drawer;
    }

    @Override
    public void onDrawerItemClick(DrawerItem drawerItem) {
        drawerCloser.closeDrawer();
        switch (drawerItem) {
            case SCHEDULE:
                changeFragment(new ScheduleFragment(), ScheduleFragment.TAG);
                break;
            case SPEAKERS:
                changeFragment(new SpeakerFragment(), SpeakerFragment.TAG);
                break;
        }
    }

    private void changeFragment(Fragment fragment, String tag) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commit();
    }
}