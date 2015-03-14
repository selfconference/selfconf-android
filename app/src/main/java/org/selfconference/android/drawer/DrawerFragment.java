package org.selfconference.android.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.selfconference.android.BaseFragment;
import org.selfconference.android.R;
import org.selfconference.android.session.SessionContainerFragment;
import org.selfconference.android.codeofconduct.CodeOfConductFragment;
import org.selfconference.android.settings.SettingsActivity;
import org.selfconference.android.speakers.SpeakerListFragment;

import butterknife.InjectView;

import static org.selfconference.android.drawer.DrawerItem.SCHEDULE;

public class DrawerFragment extends BaseFragment implements DrawerAdapter.OnDrawerItemClickListener {
    public static final String TAG = DrawerFragment.class.getName();

    @InjectView(R.id.drawer_recycler_view)
    RecyclerView drawerRecyclerView;

    private DrawerCloser drawerCloser;
    private final DrawerAdapter drawerAdapter = new DrawerAdapter();

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

        drawerAdapter.setOnDrawerItemClickListener(this);

        drawerRecyclerView.setHasFixedSize(true);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drawerRecyclerView.setAdapter(drawerAdapter);

        if (savedInstanceState == null) {
            onDrawerItemClick(SCHEDULE);
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
                changeFragment(new SessionContainerFragment(), SessionContainerFragment.TAG);
                break;
            case SPEAKERS:
                changeFragment(new SpeakerListFragment(), SpeakerListFragment.TAG);
                break;
            case CODE_OF_CONDUCT:
                changeFragment(new CodeOfConductFragment(), CodeOfConductFragment.TAG);
                break;
            case SETTINGS:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
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
