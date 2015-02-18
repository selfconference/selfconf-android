package org.selfconference.android.drawer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.selfconference.android.R;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerViewHolder> {
    public interface OnDrawerItemClickListener {
        void onDrawerItemClick(DrawerItem drawerItem);
    }
    private OnDrawerItemClickListener onDrawerItemClickListener;

    public void setOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.onDrawerItemClickListener = onDrawerItemClickListener;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_row_single_item_with_icon, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DrawerViewHolder holder, int position) {
        final DrawerItem drawerItem = DrawerItem.values()[position];

        holder.rowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                onDrawerItemClickListener.onDrawerItemClick(drawerItem);
            }
        });
        holder.icon.setImageResource(drawerItem.getIcon());
        holder.title.setText(drawerItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return DrawerItem.values().length;
    }
}
