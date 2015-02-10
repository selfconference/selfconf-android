package org.selfconference.android.drawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DrawerViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.row_item)
    View rowItem;

    @InjectView(R.id.row_icon)
    public ImageView icon;

    @InjectView(R.id.row_title)
    public TextView title;

    public DrawerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
