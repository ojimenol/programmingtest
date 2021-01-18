package com.test.springboot.rest.example.transaction.business.rules;

import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.util.FixedClock;
import java.time.Clock;
import java.time.Instant;
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

  private static final Clock TEST_CLOCK = FixedClock.of(Instant.parse("2020-01-01T00:00:00Z"));

  @Before
  public void setup() {

    transactionStatusRules = new TransactionStatusSearchRules(TEST_CLOCK);
  }

  @Test
  public void givenInexistentTransactionReturnInvalidStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A");

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, null);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.INVALID.getValue());
  }

  @Test
  public void givenTransactionFromClientChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndPastDateReturnSettledStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).minus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.SETTLED.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }

  @Test
  public void givenTransactionFromClientChannelAndTodayDateReturnPendingStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndPastDateReturnPendingStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndTodayDateReturnPendingStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }

  @Test
  public void givenTransactionFromClientChannelAndFutureDateReturnFutureStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.CLIENT.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.FUTURE.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromATMChannelAndFutureDateReturnPendingStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.ATM.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.PENDING.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount() - transaction.getFee());
    assertThat(result.getFee()).isNull();
  }

  @Test
  public void givenTransactionFromInternalChannelAndFutureDateReturnFutureStatus() {
    TransactionStatusFilterDto searchDto = new TransactionStatusFilterDto()
      .reference("00000A")
      .channel(Channels.INTERNAL.getValue());

    Transaction transaction = new Transaction()
      .reference("00000A")
      .date(OffsetDateTime.now(TEST_CLOCK).plus(1, ChronoUnit.DAYS))
      .amount(100.0)
      .fee(10.0);

    TransactionStatusDto result = transactionStatusRules.apply(searchDto, transaction);

    assertThat(result.getReference()).isEqualTo(searchDto.getReference());
    assertThat(result.getStatus()).isEqualTo(Status.FUTURE.getValue());
    assertThat(result.getAmount()).isEqualTo(transaction.getAmount());
    assertThat(result.getFee()).isEqualTo(transaction.getFee());
  }
}
