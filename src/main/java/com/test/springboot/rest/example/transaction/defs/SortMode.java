package com.test.springboot.rest.example.transaction.defs;

public enum SortMode {
  ASC("ASCENDING"),
  DESC("DESCENDING");

  private static SortMode DEFAULT_MODE = ASC;

  SortMode(String mode) {
    this.mode = mode;
  }

  private String mode;

  public String getMode() {
    return this.mode;
  }
}
