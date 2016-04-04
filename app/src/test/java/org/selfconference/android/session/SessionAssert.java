package org.selfconference.android.session;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.joda.time.ReadableDateTime;

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

  public SessionAssert hasBeginning(ReadableDateTime beginning) {
    Assertions.assertThat(actual.beginning()).isEqualTo(beginning);

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

  public SessionAssert isKeynote(boolean keynote) {
    Assertions.assertThat(actual.keynote()).isEqualTo(keynote);

    return this;
  }
}
