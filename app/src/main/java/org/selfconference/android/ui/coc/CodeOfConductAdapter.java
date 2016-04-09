package org.selfconference.android.ui.coc;

import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import org.selfconference.android.R;

import static android.text.util.Linkify.PHONE_NUMBERS;
import static android.util.Patterns.WEB_URL;

public final class CodeOfConductAdapter
    extends RecyclerView.Adapter<CodeOfConductAdapter.ViewHolder> {

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_code_of_conduct_card, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Code code = Code.values()[position];

    holder.title.setText(code.getTitle());
    holder.subtitle.setText(code.getSubtitle());

    linkifySubtitleText(holder);
  }

  @Override public int getItemCount() {
    return Code.values().length;
  }

  private static void linkifySubtitleText(ViewHolder holder) {
    Linkify.addLinks(holder.subtitle, PHONE_NUMBERS);
    Linkify.addLinks(holder.subtitle, WEB_URL, null, new SelfConferenceUrlDenier(), null);
  }

  static final class ViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.code_of_conduct_title) TextView title;
    @Bind(R.id.code_of_conduct_subtitle) TextView subtitle;

    ViewHolder(View itemView) {
      super(itemView);
    }
  }

  private static final class SelfConferenceUrlDenier implements MatchFilter {
    private static final String FAKE_SELF_CONFERENCE_DOMAIN = "self.conference";

    @Override public boolean acceptMatch(CharSequence s, int start, int end) {
      String match = s.subSequence(start, end).toString();
      return !match.equalsIgnoreCase(FAKE_SELF_CONFERENCE_DOMAIN);
    }
  }
}
