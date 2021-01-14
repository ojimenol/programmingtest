package com.test.springboot.rest.example.transaction.defs;

public enum Channels {
  CLIENT("CLIENT"),ATM("ATM"),INTERNAL("INTERNAL");

  Channels(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  private String value;
}
