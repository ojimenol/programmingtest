package com.test.springboot.rest.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotNull;

public class TransactionStatusDto {
  @NotNull
  private String reference;
  @JsonInclude(Include.NON_NULL)
  private String channel;
  @JsonInclude(Include.NON_NULL)
  private String status;
  @JsonInclude(Include.NON_NULL)
  private Double amount;
  @JsonInclude(Include.NON_NULL)
  private Double fee;

  public String getReference() { return this.reference; }
  public void setReference(String ref) { this.reference = ref; }

  public String getChannel() { return this.channel; }
  public void setChannel(String channel) { this.channel = channel; }

  public String getStatus() { return this.status; }
  public void setStatus(String status) { this.status = status; }

  public Double getAmount() { return this.amount; }
  public void setAmount(Double amount) { this.amount = amount; }

  public Double getFee() { return this.fee; }
  public void setFee(Double fee) { this.fee = fee; }

  public TransactionStatusDto reference(String ref) { this.reference = ref; return this; }

  public TransactionStatusDto channel(String channel) { this.channel = channel; return this; }

  public TransactionStatusDto status(String status) { this.status = status; return this; }

  public TransactionStatusDto amount(Double amount) { this.amount = amount; return this; }

  public TransactionStatusDto fee(Double fee) { this.fee = fee; return this; }

  public TransactionStatusDto clone() {
    return new TransactionStatusDto()
      .reference(this.getReference())
      .channel(this.getChannel())
      .status(this.status)
      .amount(this.amount)
      .fee(this.fee);
  }
}
