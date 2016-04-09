package org.selfconference.android.ui.misc;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;

public abstract class ButterKnifeViewHolder extends RecyclerView.ViewHolder {
  protected ButterKnifeViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
