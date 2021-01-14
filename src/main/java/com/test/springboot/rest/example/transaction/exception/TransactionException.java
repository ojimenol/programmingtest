package com.test.springboot.rest.example.transaction.exception;

import com.test.springboot.rest.example.transaction.defs.Error;

public class TransactionException extends RuntimeException {
  private String code;
  private String description;

  public TransactionException() {

  }

  public TransactionException(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public TransactionException(Error error) {
    this.code = error.getCode();
    this.description = error.getDescription();
  }

  public String getCode() {
    return this.code;
  }

  public String getDescription() {
    return this.description;
  }

}
