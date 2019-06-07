package org.selfconference.data.pref;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.selfconference.data.api.model.Room;
import org.selfconference.data.api.model.Session;
import org.selfconference.data.api.model.Slot;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public final class SessionPreferencesTest {

  private Context context;
  private SessionPreferences preferences;

  @Before public void setUp() {
    context = ApplicationProvider.getApplicationContext();
    preferences = new SessionPreferences(context);
  }

  @Test public void testSavedSessionPreferences() {
    Session session = Session.builder()
        .id(13)
        .name("Title")
        .room(Room.empty())
        .description("Description")
        .keynote(false)
        .slot(Slot.empty())
        .speakers(ImmutableList.of())
        .build();

    assertThat(preferences.isFavorite(session)).isFalse();

    preferences.favorite(session);

    assertThat(preferences.isFavorite(session)).isTrue();

    preferences.unfavorite(session);

    assertThat(preferences.isFavorite(session)).isFalse();
  }
}
