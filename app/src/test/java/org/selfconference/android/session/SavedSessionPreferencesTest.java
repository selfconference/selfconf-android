package org.selfconference.android.session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.selfconference.android.CustomTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CustomTestRunner.class)
@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
public class SavedSessionPreferencesTest {

    private SavedSessionPreferences preferences;

    @Before
    public void setUp() throws Exception {
        preferences = new SavedSessionPreferences(Robolectric.application);
    }

    @Test
    public void testSavedSessionPreferences() throws Exception {
        final Session session = Session.builder()
                .id(13)
                .build();

        assertThat(preferences.isFavorite(session)).isFalse();

        preferences.saveFavorite(session);

        assertThat(preferences.isFavorite(session)).isTrue();

        preferences.removeFavorite(session);

        assertThat(preferences.isFavorite(session)).isFalse();
    }
}