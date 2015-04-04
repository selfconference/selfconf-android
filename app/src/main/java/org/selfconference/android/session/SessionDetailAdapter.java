package org.selfconference.android.session;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.R;

import java.util.List;

import butterknife.InjectView;

public final class SessionDetailAdapter extends RecyclerView.Adapter<SessionDetailAdapter.ViewHolder> {

    private final List<SessionDetail> sessionDetails;

    public SessionDetailAdapter(List<SessionDetail> sessionDetails) {
        this.sessionDetails = Optional.fromNullable(sessionDetails).or(Lists.<SessionDetail>newArrayList());
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_session_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final SessionDetail sessionDetail = sessionDetails.get(position);

        holder.icon.setImageDrawable(sessionDetail.drawable);
        holder.title.setText(sessionDetail.info);
    }

    @Override public int getItemCount() {
        return sessionDetails.size();
    }

    public static class ViewHolder extends ButterKnifeViewHolder {

        @InjectView(R.id.row_icon) public ImageView icon;
        @InjectView(R.id.row_title) public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
