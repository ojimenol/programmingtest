package com.test.springboot.rest.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class TransactionResponse<T> {

  private static final String OK_CODE = "0000";
  private static final String OK_DESCRIPTION = "Operation completed sucessfully";

  private T response;
  private String errorCode;
  private String errorDescription;

  public TransactionResponse() { }

  public TransactionResponse(T response) {
    this.response = response;
  }

  public TransactionResponse(String code, String description) {
    this.errorCode = code;
    this.errorDescription = description;
  }

  public TransactionResponse<T> ok() {
    this.errorCode = OK_CODE;
    this.errorDescription = OK_DESCRIPTION;
    return this;
  }

  @JsonInclude(Include.NON_NULL)
  public T getResponse() { return this.response; }
  public void setResponse(T response) { this.response = response; }

  public TransactionResponse<T> response (T response) { this.response = response; return this; }

  @JsonInclude(Include.NON_NULL)
  public String getErrorCode() { return this.errorCode; }
  public void setErrorCode(String code) { this.errorCode = code; }

  public TransactionResponse<T> errorCode (String code) { this.errorCode = code; return this; }

  @JsonInclude(Include.NON_NULL)
  public String getErrorDescription() { return this.errorDescription; }
  public void setErrorDescription(String description) { this.errorDescription = description; }

  public TransactionResponse<T> errorDescription (String description) { this.errorDescription = description; return this; }
}
