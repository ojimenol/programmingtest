package com.test.springboot.rest.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import javax.validation.constraints.NotNull;

public class TransactionSearch {

  @NotNull
  @JsonAlias("account_iban")
  private String accountIban;
  private String sort;

  public String getAccountIban() { return this.accountIban; }
  public void setAccountIban(String iban) { this.accountIban = iban; }

  public String getSort() { return this.sort; }
  public void setSort(String mode) { this.sort = mode; }

}
