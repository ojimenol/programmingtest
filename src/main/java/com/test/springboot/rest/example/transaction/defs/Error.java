package com.test.springboot.rest.example.transaction.defs;

public enum Error {
  DEFAULT_ERROR_MESSAGE("0100","Unknown error creating transaction"),
  ACCOUNT_NOT_FOUND("0101","Account not found"),
  CREATE_TRANSACTION_FAIL("0200","Can't create transaction."),
  CREATE_TRANSACTION_REFERENCE_EXISTS("0201","Reference for this transaction exists. Can't create transaction"),
  CREATE_TRANSACTION_POST_BALANCE_NEGATIVE("0202","Negative Post Balance for transaction. Transaction rejected");

  Error(String code, String description) {
    this.code = code; this.description = description;
  }

  public String getCode() { return this.code; }
  public String getDescription() { return this.description; }

  private String code;
  private String description;
}
