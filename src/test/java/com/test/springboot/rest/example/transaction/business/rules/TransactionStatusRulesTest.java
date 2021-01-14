package com.test.springboot.rest.example.transaction.business.rules;

import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilter;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionStatus;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TransactionStatusRulesTest {

  private TransactionStatusSearchRules transactionStatusRules;

  @Before
  public void setup() {
    transactionStatusRules = new TransactionStatusSearchRules();
  }

  @Test
  public void givenInexistentTransactionReturnInvalidStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A");

    TransactionStatus result = transactionStatusRules.apply(searchDto, null);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.INVALID.getValue());
  }

  @Test
  public void givenTransactionFromClientChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }

  @Test
  public void givenTransactionFromClientChannelAndTodayDateReturnPendingStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now())
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndPastDateReturnPendingStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now())
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndTodayDateReturnPendingStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now())
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }

  @Test
  public void givenTransactionFromClientChannelAndFutureDateReturnFutureStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.FUTURE.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndFutureDateReturnPendingStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndFutureDateReturnFutureStatus() {
    TransactionStatusFilter searchDto = new TransactionStatusFilter()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now().plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatus result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.FUTURE.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }
}
