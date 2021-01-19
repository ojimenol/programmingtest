package com.test.springboot.rest.example.account.persistent;

import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACCOUNT")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Column(name = "IBAN", length = 24, nullable = false, unique = true)
  private String iban;

  @NotNull
  @Column(name = "BALANCE", nullable = false, unique = false)
  private Double balance;

  @NotNull
  @OneToMany(targetEntity = Transaction.class)
  private List<Transaction> transactionList;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Account iban(String iban) {
    this.iban = iban;
    return this;
  }

  public Account balance(Double balance) {
    this.balance = balance;
    return this;
  }
}
