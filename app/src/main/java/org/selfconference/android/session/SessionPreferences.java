package org.selfconference.android.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Set;
import org.selfconference.android.data.api.model.Session;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.collect.Sets.newHashSet;
import static org.selfconference.android.BuildConfig.APPLICATION_ID;

public final class SessionPreferences {
  private static final String PREFS_NAME = APPLICATION_ID + ".sessions";
  private static final String KEY_FAVORITES = "favorites";
  private static final String KEY_FEEDBACKS = "feedbacks";

  private final SharedPreferences sharedPreferences;

  public SessionPreferences(Context context) {
    this.sharedPreferences =
        context.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
  }

  public void favorite(Session session) {
    Timber.d("Add session to favorites: %s", session);

    Set<String> favorites = getFavorites();
    favorites.add(idAsString(session));

    saveFavorites(favorites);
  }

  public void unfavorite(Session session) {
    Timber.d("Remove session from favorites: %s", session);

    Set<String> favorites = getFavorites();
    favorites.remove(idAsString(session));

    saveFavorites(favorites);
  }

  public void submitFeedback(Session session) {
    Timber.d("Add session to feedback: %s", session);

    Set<String> feedbacks = getFeedbacks();
    feedbacks.add(idAsString(session));

    saveFeedbacks(feedbacks);
  }

  public boolean hasSubmittedFeedback(Session session) {
    return getFeedbacks().contains(idAsString(session));
  }

  public boolean isFavorite(Session session) {
    return getFavorites().contains(idAsString(session));
  }

  private void saveFavorites(Set<String> favorites) {
    Timber.d("Favorited sessions: %s", Iterables.toString(favorites));
    updatePreferences(KEY_FAVORITES, favorites);
  }

  public boolean hasFavorites() {
    return !getFavorites().isEmpty();
  }

  private Set<String> getFavorites() {
    return getSessionsForKey(KEY_FAVORITES);
  }

  private void saveFeedbacks(Set<String> feedbacks) {
    Timber.d("Submitted feedback for sessions: %s", Iterables.toString(feedbacks));
    updatePreferences(KEY_FEEDBACKS, feedbacks);
  }

  private Set<String> getFeedbacks() {
    return getSessionsForKey(KEY_FEEDBACKS);
  }

  private void updatePreferences(String key, Set<String> savedSessions) {
    sharedPreferences.edit().putStringSet(key, savedSessions).apply();
  }

  @SuppressWarnings("ConstantConditions") private Set<String> getSessionsForKey(String key) {
    return newHashSet(sharedPreferences.getStringSet(key, Sets.<String>newHashSet()));
  }

  private static String idAsString(Session session) {
    return String.valueOf(session.id());
  }
}
