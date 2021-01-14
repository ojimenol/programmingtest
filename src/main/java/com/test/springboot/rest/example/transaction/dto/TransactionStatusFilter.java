package com.test.springboot.rest.example.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotNull;

public class TransactionStatusFilter {
  @NotNull
  private String reference;
  @JsonInclude(Include.NON_NULL)
  private String channel;

  public String getReference() { return this.reference; }
  public void setReference(String ref) { this.reference = ref; }

  public String getChannel() { return this.channel; }
  public void setChannel(String channel) { this.channel = channel; }

  public TransactionStatusFilter reference(String ref) { this.reference = ref; return this; }

  public TransactionStatusFilter channel(String channel) { this.channel = channel; return this; }
}
