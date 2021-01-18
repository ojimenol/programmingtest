package com.test.springboot.rest.example.transaction.defs;

import java.util.stream.Stream;

public enum Error {
  DEFAULT_ERROR_MESSAGE("0100","Unknown transaction error"),
  ACCOUNT_NOT_FOUND("0101","Account not found"),
  CREATE_TRANSACTION_FAIL("0200","Can't create transaction."),
  GENERATE_TRANSACTION_REFERENCES_FULL("0201","Can't generate reference. Can't found free references"),
  CREATE_TRANSACTION_REFERENCE_EXISTS("0202","Reference for this transaction exists. Can't create transaction"),
  CREATE_TRANSACTION_POST_BALANCE_NEGATIVE("0203","Negative Post Balance for transaction. Transaction rejected");

  Error(String code, String description) {
    this.code = code; this.description = description;
  }

  public String getCode() { return this.code; }
  public String getDescription() { return this.description; }

  private String code;
  private String description;

  public static Error findErrorByCode(String code) {
    return Stream.of(Error.values())
      .filter(error -> error.getCode().equals(code))
      .findFirst()
      .orElse(Error.DEFAULT_ERROR_MESSAGE);

  }
}
