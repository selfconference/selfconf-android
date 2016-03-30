package org.selfconference.android.session;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public final class SessionPreferencesTest {

  private SessionPreferences preferences;

  @Before public void setUp() throws Exception {
    preferences = new SessionPreferences(Robolectric.application);
  }

  @Test public void testSavedSessionPreferences() throws Exception {
    Session session = Session.builder() //
        .id(13)
        .title("Title")
        .room(Room.emptyRoom())
        .description("Description")
        .keynote(false)
        .speakers(ImmutableList.of())
        .build();

    assertThat(preferences.isFavorite(session)).isFalse();

    preferences.favorite(session);

    assertThat(preferences.isFavorite(session)).isTrue();

    preferences.unfavorite(session);

    assertThat(preferences.isFavorite(session)).isFalse();
  }
}
