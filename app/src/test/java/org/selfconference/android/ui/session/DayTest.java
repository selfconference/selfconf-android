package org.selfconference.ui.session;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class DayTest {

  @Test public void zeroPositionEquatesToDayOne() throws Exception {
    assertThat(Day.fromPosition(0)).isEqualTo(Day.ONE);
  }

  @Test public void firstPositionEquatesToDayTwo() throws Exception {
    assertThat(Day.fromPosition(1)).isEqualTo(Day.TWO);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonZeroOrFirstPositionThrowsException() throws Exception {
    assertThat(Day.fromPosition(3)).isNotNull();
  }
}
