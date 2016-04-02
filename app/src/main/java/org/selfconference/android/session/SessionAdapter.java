package org.selfconference.android.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import java.util.Locale;
import javax.inject.Inject;
import org.selfconference.android.ButterKnifeViewHolder;
import org.selfconference.android.FilterableAdapter;
import org.selfconference.android.FilteredDataSubscriber;
import org.selfconference.android.R;
import org.selfconference.android.api.Api;
import rx.functions.Func1;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.selfconference.android.utils.DateStringer.toShortDateString;

public class SessionAdapter extends FilterableAdapter<Session, SessionAdapter.SessionViewHolder> {
  public interface OnSessionClickListener {
    void onSessionClick(Session event);
  }

  @Inject Api api;
  @Inject SessionPreferences preferences;

  private OnSessionClickListener onSessionClickListener;

  public void setOnSessionClickListener(OnSessionClickListener onSessionClickListener) {
    this.onSessionClickListener = onSessionClickListener;
  }

  public void refresh() {
    notifyDataSetChanged();
  }

  @Override public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.include_session_row, parent, false);
    return new SessionViewHolder(view);
  }

  @Override public void onBindViewHolder(SessionViewHolder holder, int position) {
    Session session = getFilteredData().get(position);

    holder.itemView.setOnClickListener(v -> {
      if (onSessionClickListener != null) {
        onSessionClickListener.onSessionClick(session);
      }
    });

    holder.favoriteSessionIndicator.setVisibility(preferences.isFavorite(session) ? VISIBLE : GONE);

    holder.sessionTitle.setText(session.title());
    holder.sessionSubtitle.setText(session.room().name());
    try {
      Session previousSession = getFilteredData().get(position - 1);
      if (session.beginning() != null) {
        if (session.beginning().isEqual(previousSession.beginning())) {
          holder.startTime.setVisibility(INVISIBLE);
        } else {
          setStartTime(holder, session);
        }
      }
    } catch (IndexOutOfBoundsException e) {
    }
  }

  public void filterFavorites(boolean show) {
    getFilteredData().clear();
    dataObservable() //
        .filter(session -> !show || preferences.isFavorite(session)) //
        .subscribe(new FilteredDataSubscriber<>(this));
  }

  @Override protected Func1<Session, Boolean> filterPredicate(String query) {
    return session -> session.title() //
        .toLowerCase(Locale.US) //
        .contains(query.toLowerCase(Locale.US));
  }

  private static void setStartTime(SessionViewHolder holder, Session session) {
    holder.startTime.setText(toShortDateString(session.beginning()));
    holder.startTime.setVisibility(VISIBLE);
  }

  static final class SessionViewHolder extends ButterKnifeViewHolder {

    @Bind(R.id.start_time) TextView startTime;
    @Bind(R.id.slot_title) TextView sessionTitle;
    @Bind(R.id.slot_subtitle) TextView sessionSubtitle;
    @Bind(R.id.favorite_session_indicator) View favoriteSessionIndicator;

    SessionViewHolder(View itemView) {
      super(itemView);
    }
  }
}
