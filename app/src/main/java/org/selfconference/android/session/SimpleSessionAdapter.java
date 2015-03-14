package org.selfconference.android.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.selfconference.android.R;
import org.selfconference.android.api.Session;

import java.util.List;

import static butterknife.ButterKnife.findById;

public final class SimpleSessionAdapter extends ArrayAdapter<Session> {

    public SimpleSessionAdapter(Context context, List<Session> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.two_line_list_item, parent, false);
        final TextView title = findById(view, android.R.id.text1);
        final TextView subtitle = findById(view, android.R.id.text2);

        final Session session = getItem(position);

        title.setText(session.getTitle());
        subtitle.setText(session.getRoom());

        return view;
    }
}
