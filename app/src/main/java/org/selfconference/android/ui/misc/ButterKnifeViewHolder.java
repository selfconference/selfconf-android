package org.selfconference.android.ui.misc;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public abstract class ButterKnifeViewHolder extends RecyclerView.ViewHolder {
  protected ButterKnifeViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
