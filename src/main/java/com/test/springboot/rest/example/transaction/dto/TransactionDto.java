package com.test.springboot.rest.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TransactionDto {

  @Size(min = 6, message = "Name should have at least 6 characters")
  private String reference;

  @NotNull
  @Size(min = 24, max = 24, message = "IBAN should have 24 characters")
  @JsonAlias("account_iban")
  private String accountIban;

  private OffsetDateTime date;

  @NotNull
  private Double amount;

  private Double fee;

  private String description;

  public TransactionDto() {
  }

  public String getReference() {
    return this.reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getAccountIban() {
    return this.accountIban;
  }

  public void setAccountIban(String iban) {
    this.accountIban = iban;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Double getFee() {
    return fee;
  }

  public void setFee(Double fee) {
    this.fee = fee;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TransactionDto reference(String ref) {
    this.reference = ref;
    return this;
  }

  public TransactionDto accountIban(String iban) {
    this.accountIban = iban;
    return this;
  }

  public TransactionDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  public TransactionDto amount(Double amount) {
    this.amount = amount;
    return this;
  }

  public TransactionDto fee(Double fee) {
    this.fee = fee;
    return this;
  }

  public TransactionDto description(String desc) {
    this.description = desc;
    return this;
  }

  public TransactionDto clone() {
    return new TransactionDto()
      .reference(this.reference)
      .accountIban(this.accountIban)
      .date(this.date)
      .amount(this.amount)
      .fee(this.fee);
  }
}
