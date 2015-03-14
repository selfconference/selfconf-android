package org.selfconference.android.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import org.selfconference.android.api.Session;

import java.util.Set;

import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.collect.Sets.newHashSet;

public class SavedSessionPreferences {
    private static final String PREFS_NAME = "savedSessions";
    private static final String KEY_SESSIONS = "sessions";

    private final SharedPreferences sharedPreferences;

    public SavedSessionPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public void saveFavorite(Session session) {
        Timber.d("Add session to favorites: %s", session);

        final Set<String> savedSessions = getSavedSessions();
        savedSessions.add(idAsString(session));

        updatePreferences(savedSessions);
    }

    public void removeFavorite(Session session) {
        Timber.d("Remove session from favorites: %s", session);

        final Set<String> savedSessions = getSavedSessions();
        savedSessions.remove(idAsString(session));

        updatePreferences(savedSessions);
    }

    private void updatePreferences(Set<String> savedSessions) {
        Timber.d("Saved sessions: %s", Iterables.toString(savedSessions));

        sharedPreferences.edit().putStringSet(KEY_SESSIONS, savedSessions).apply();
    }

    public boolean isFavorite(Session session) {
        return getSavedSessions().contains(idAsString(session));
    }

    @SuppressWarnings("ConstantConditions")
    private Set<String> getSavedSessions() {
        return newHashSet(sharedPreferences.getStringSet(KEY_SESSIONS, Sets.<String>newHashSet()));
    }

    private static String idAsString(Session session) {
        return String.valueOf(session.getId());
    }
}
