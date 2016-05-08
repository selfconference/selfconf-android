package org.selfconference.android.ui.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.google.common.base.Optional;
import java.util.Locale;
import org.selfconference.android.R;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.selfconference.android.data.api.model.Slot;
import org.selfconference.android.data.pref.SessionPreferences;
import org.selfconference.android.ui.misc.ButterKnifeViewHolder;
import org.selfconference.android.ui.misc.FilterableAdapter;
import org.selfconference.android.ui.misc.FilteredDataSubscriber;
import org.selfconference.android.util.Instants;
import rx.functions.Func1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SessionAdapter extends FilterableAdapter<Session, SessionAdapter.SessionViewHolder> {
  public interface OnSessionClickListener {
    void onSessionClick(Session event);
  }

  private final SessionPreferences preferences;

  private OnSessionClickListener onSessionClickListener;

  public SessionAdapter(SessionPreferences preferences) {
    super();
    this.preferences = preferences;
  }

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
    final Session session = getFilteredData().get(position);

    holder.itemView.setOnClickListener(v -> {
      if (onSessionClickListener != null) {
        onSessionClickListener.onSessionClick(session);
      }
    });

    holder.favoriteSessionIndicator.setVisibility(preferences.isFavorite(session) ? VISIBLE : GONE);

    holder.sessionTitle.setText(session.name());
    Room room = Optional.fromNullable(session.room()).or(Room.empty());
    holder.sessionSubtitle.setText(room.name());
    Slot slot = Optional.fromNullable(session.slot()).or(Slot.empty());
    holder.startTime.setText(Instants.miniTimeString(slot.time()));
  }

  public void filterFavorites(final boolean show) {
    getFilteredData().clear();
    dataObservable() //
        .filter(session -> !show || preferences.isFavorite(session)) //
        .subscribe(new FilteredDataSubscriber<>(this));
  }

  @Override protected Func1<Session, Boolean> filterPredicate(final String query) {
    return session -> session.name() //
        .toLowerCase(Locale.US) //
        .contains(query.toLowerCase(Locale.US));
  }

  static final class SessionViewHolder extends ButterKnifeViewHolder {

    @BindView(R.id.start_time) TextView startTime;
    @BindView(R.id.slot_title) TextView sessionTitle;
    @BindView(R.id.slot_subtitle) TextView sessionSubtitle;
    @BindView(R.id.favorite_session_indicator) View favoriteSessionIndicator;

    SessionViewHolder(View itemView) {
      super(itemView);
    }
  }
}
