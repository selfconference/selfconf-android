package org.selfconference.android.ui.decorators;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.selfconference.android.data.api.NullDateTime;
import org.selfconference.android.ui.decorators.DateTimeDecorator.NullDateTimeDecorator;
import org.selfconference.android.ui.decorators.DateTimeDecorator.ValidDateTimeDecorator;
import org.selfconference.android.utils.DateTimes;

import static org.assertj.core.api.Assertions.assertThat;

public final class DateTimeDecoratorTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test public void dateTimeDecoratorImplementationsDoNotAcceptNullDateTimeParameter() {
    expectedException.expect(NullPointerException.class);

    DateTimeDecorator.fromDateTime(null);
  }

  @Test public void factoryMethod_returnsNullDecorator_whenAcceptingNullDateTimeParameter() {
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(NullDateTime.create());

    assertThat(dateTimeDecorator).isInstanceOf(NullDateTimeDecorator.class);
  }

  @Test public void factoryMethod_returnsValidDecorator_whenAcceptingValidDateTimeParameter() {
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(DateTime.now());

    assertThat(dateTimeDecorator).isInstanceOf(ValidDateTimeDecorator.class);
  }

  @Test public void validDateTime_fullDateString_displaysProperly() {
    DateTime dateTime = new DateTime(2016, 4, 4, 15, 0, DateTimes.EST);

    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(dateTime);

    assertThat(dateTimeDecorator.fullDateString()).isEqualTo("Apr 4 / 3:00 PM");
  }

  @Test public void validDateTime_shortTimeString_displaysProperly() {
    DateTime dateTime = new DateTime(2016, 1, 2, 11, 0, DateTimes.EST);

    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(dateTime);

    assertThat(dateTimeDecorator.shortTimeString()).isEqualTo("11A");
  }

  @Test public void validDateTime_shortDateString_displaysProperly() {
    DateTime dateTime = new DateTime(2016, 7, 21, 15, 0, DateTimes.EST);

    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(dateTime);

    assertThat(dateTimeDecorator.shortDateString()).isEqualTo("Jul 21");
  }

  @Test public void nullDateTime_fullDateString_displaysProperly() {
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(NullDateTime.create());

    assertThat(dateTimeDecorator.fullDateString()).isEqualTo("TBD");
  }

  @Test public void nullDateTime_shortTimeString_displaysProperly() {
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(NullDateTime.create());

    assertThat(dateTimeDecorator.shortTimeString()).isEqualTo("TBD");
  }

  @Test public void nullDateTime_shortDateString_displaysProperly() {
    DateTimeDecorator dateTimeDecorator = DateTimeDecorator.fromDateTime(NullDateTime.create());

    assertThat(dateTimeDecorator.shortDateString()).isEqualTo("TBD");
  }
}
