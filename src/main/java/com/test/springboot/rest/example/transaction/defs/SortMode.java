package com.test.springboot.rest.example.transaction.defs;

import java.util.stream.Stream;

public enum SortMode {
  ASC("ASCENDING"),
  DESC("DESCENDING");

  SortMode(String mode) {
    this.mode = mode;
  }

  private String mode;

  public String getMode() {
    return this.mode;
  }

  public static SortMode getSortByMode(String mode) {
    return Stream.of(SortMode.values())
      .filter(value -> value.getMode().equals(mode))
      .findFirst()
      .orElse(SortMode.ASC);
  }
}
