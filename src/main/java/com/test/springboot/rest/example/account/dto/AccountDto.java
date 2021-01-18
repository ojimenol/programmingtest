package com.test.springboot.rest.example.account.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDto {

  @NotNull
  @Size(min = 24, max = 24, message = "IBAN should have 24 characters")
  private String iban;

  @NotNull
  private Double balance;

  public String getIban() {
    return this.iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public AccountDto iban(String iban) {
    this.iban = iban;
    return this;
  }

  public AccountDto balance(Double balance) {
    this.balance = balance;
    return this;
  }
}
