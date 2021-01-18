package com.test.springboot.rest.example.transaction.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "REFERENCE", length = 6, nullable = false, unique = true)
  private String reference;

  @NotNull
  @Column(name = "ACCOUNT_IBAN", length = 24, nullable = false)
  private String accountIban;

  @Column(name = "DATE")
  private OffsetDateTime date;

  @NotNull
  @Column(name = "AMOUNT", nullable = false)
  private Double amount;

  @Column(name = "FEE")
  private Double fee;

  @Column(name = "DESCRIPTION")
  private String description;

  public Transaction() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Transaction reference(String ref) {
    this.reference = ref;
    return this;
  }

  public Transaction accountIban(String iban) {
    this.accountIban = iban;
    return this;
  }

  public Transaction date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  public Transaction amount(Double amount) {
    this.amount = amount;
    return this;
  }

  public Transaction fee(Double fee) {
    this.fee = fee;
    return this;
  }

  public Transaction description(String desc) {
    this.description = desc;
    return this;
  }

  public Transaction clone() {
    return new Transaction()
      .reference(this.reference)
      .accountIban(this.accountIban)
      .date(this.date)
      .amount(this.amount)
      .fee(this.fee);
  }
}
