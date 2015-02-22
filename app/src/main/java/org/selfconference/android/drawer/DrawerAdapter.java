package org.selfconference.android.drawer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.OnClickListener;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
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

        holder.itemView.setOnClickListener(new OnClickListener() {
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

    public static class DrawerViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.row_icon)
        public ImageView icon;

        @InjectView(R.id.row_title)
        public TextView title;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
