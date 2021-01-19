package com.test.springboot.rest.example.transaction.persistent;

import com.test.springboot.rest.example.account.persistent.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @OneToOne(targetEntity = Reference.class)
  private Reference reference;

  @NotNull
  @ManyToOne(targetEntity = Account.class)
  private Account account;

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

  public Reference getReference() {
    return this.reference;
  }

  public void setReference(Reference reference) {
    this.reference = reference;
  }

  public Account getAccount() {
    return this.account;
  }

  public void setAccount(Account account) {
    this.account = account;
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

  public Transaction reference(Reference ref) {
    this.reference = ref;
    return this;
  }

  public Transaction account(Account account) {
    this.account = account;
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
      .date(this.date)
      .amount(this.amount)
      .fee(this.fee);
  }
}
