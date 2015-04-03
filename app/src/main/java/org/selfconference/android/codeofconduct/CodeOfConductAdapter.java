package org.selfconference.android.codeofconduct;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.R;

import butterknife.InjectView;

public class CodeOfConductAdapter extends RecyclerView.Adapter<CodeOfConductAdapter.ViewHolder> {

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_code_of_conduct_card, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        final Code code = Code.values()[position];

        holder.title.setText(code.getTitle());
        holder.subtitle.setText(code.getSubtitle());
    }

    @Override public int getItemCount() {
        return Code.values().length;
    }

    public static final class ViewHolder extends ButterKnifeViewHolder {

        @InjectView(R.id.code_of_conduct_title) public TextView title;
        @InjectView(R.id.code_of_conduct_subtitle) public TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
