package org.selfconference.android.drawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.R;

import butterknife.InjectView;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.DEFAULT;
import static android.graphics.Typeface.NORMAL;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.View.OnClickListener;
import static org.selfconference.android.drawer.DrawerItem.SETTINGS;
import static org.selfconference.android.utils.ResourceProvider.getColor;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {
    public interface OnDrawerItemClickListener {
        void onDrawerItemClick(DrawerItem drawerItem);
    }

    private static final int SELECTED_COLOR = getColor(R.color.purple);
    private static final int UNSELECTED_TEXT_COLOR = getColor(R.color.text_tint);
    private static final int UNSELECTED_IMAGE_COLOR = getColor(R.color.image_tint);

    private static int selectableItemBackgroundResId = -1;

    private OnDrawerItemClickListener onDrawerItemClickListener;
    private int selectedPosition = -1;

    public void setOnDrawerItemClickListener(OnDrawerItemClickListener onDrawerItemClickListener) {
        this.onDrawerItemClickListener = onDrawerItemClickListener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_row_single_item_with_icon, parent, false);
        return new DrawerViewHolder(view);
    }

    @Override public void onBindViewHolder(final DrawerViewHolder holder, final int position) {
        final DrawerItem drawerItem = DrawerItem.values()[position];

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(@NonNull View v) {
                if (onDrawerItemClickListener != null) {
                    onDrawerItemClickListener.onDrawerItemClick(drawerItem);
                }
            }
        });
        holder.icon.setImageResource(drawerItem.getIcon());
        holder.title.setText(drawerItem.getTitle());

        if (position != SETTINGS.ordinal()) {
            final boolean isSelected = selectedPosition == position;

            holder.itemView.setBackgroundResource(isSelected ?
                    R.drawable.item_background :
                    getSelectableItemBackgroundResId(holder.itemView.getContext()));

            if (SDK_INT >= LOLLIPOP) {
                holder.icon.getDrawable().setTint(isSelected ?
                        SELECTED_COLOR :
                        UNSELECTED_IMAGE_COLOR);
                holder.title.setTextColor(isSelected ?
                        SELECTED_COLOR :
                        UNSELECTED_TEXT_COLOR);
            } else {
                holder.title.setTypeface(DEFAULT, isSelected ? BOLD : NORMAL);
            }
        }
    }

    private static int getSelectableItemBackgroundResId(Context context) {
        if (selectableItemBackgroundResId == -1) {
            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.selectableItemBackground, typedValue, true);
            selectableItemBackgroundResId = typedValue.resourceId;
        }
        return selectableItemBackgroundResId;
    }

    @Override public int getItemCount() {
        return DrawerItem.values().length;
    }

    public static class DrawerViewHolder extends ButterKnifeViewHolder {

        @InjectView(R.id.row_icon) public ImageView icon;
        @InjectView(R.id.row_title) public TextView title;

        public DrawerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
