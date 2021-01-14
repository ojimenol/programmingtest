package com.test.springboot.rest.example.transaction.defs;

public enum Status {
  PENDING("PENDING"),SETTLED("SETTLED"), FUTURE("FUTURE"), INVALID("INVALID");

  Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  private String value;
}
