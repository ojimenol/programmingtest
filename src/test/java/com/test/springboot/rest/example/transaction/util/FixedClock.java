package com.test.springboot.rest.example.transaction.util;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class FixedClock extends Clock {

  private Instant instant;

  public static FixedClock of(Instant instant) {
    return new FixedClock(instant);
  }

  private FixedClock(Instant instant) {
    this.instant = instant;
  }

  @Override
  public ZoneId getZone() {
    return ZoneId.of(ZoneOffset.UTC.getId());
  }

  @Override
  public Clock withZone(ZoneId zone) {
    return this;
  }

  @Override
  public Instant instant() {
    return instant;
  }
}
