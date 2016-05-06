package org.selfconference.android.support.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.selfconference.android.data.api.model.Room;
import org.selfconference.android.data.api.model.Session;
import org.threeten.bp.Instant;

public final class SessionAssert extends AbstractAssert<SessionAssert, Session> {

  public static SessionAssert assertThat(Session actual) {
    return new SessionAssert(actual);
  }

  protected SessionAssert(Session actual) {
    super(actual, SessionAssert.class);
  }

  public SessionAssert hasId(int id) {
    Assertions.assertThat(actual.id()).isEqualTo(id);

    return this;
  }

  public SessionAssert hasName(String name) {
    Assertions.assertThat(actual.title()).isEqualTo(name);

    return this;
  }

  public SessionAssert hasRoom(Room room) {
    Assertions.assertThat(actual.room()).isEqualTo(room);

    return this;
  }

  public SessionAssert hasBeginning(Instant beginning) {
    Assertions.assertThat(actual.slot()).isEqualTo(beginning);

    return this;
  }

  public SessionAssert hasDescription(String description) {
    Assertions.assertThat(actual.description()).isEqualTo(description);

    return this;
  }

  public SessionAssert hasSpeakersSize(int size) {
    Assertions.assertThat(actual.speakers()).hasSize(size);

    return this;
  }

  public SessionAssert isKeynote() {
    Assertions.assertThat(actual.keynote()).isEqualTo(true);

    return this;
  }

  public SessionAssert isNotKeynote() {
    Assertions.assertThat(actual.keynote()).isEqualTo(false);

    return this;
  }
}
