package com.test.springboot.rest.example.transaction.business.rules;

import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class TransactionStatusSearchRules {

  private Clock clock;

  public TransactionStatusSearchRules(Clock clock) {
    this.clock = clock;
  }

  private Predicate<TransactionStatusFilterDto> CLIENT_OR_ATM_CHANNEL_FILTER =
    status-> status.getChannel().equals(Channels.ATM.getValue()) || status.getChannel().equals(Channels.CLIENT.getValue());

  private Predicate<TransactionStatusFilterDto> INTERNAL_CHANNEL_FILTER =
    status -> status.getChannel().equals(Channels.INTERNAL.getValue());

  private Predicate<Transaction> NOT_TRANSACTION_FOUND = Objects::isNull;

  private Predicate<Transaction> TRANSACTION_PAST =
    trans -> trans.getDate().toLocalDate().isBefore(OffsetDateTime.now(clock).toLocalDate());

  private Predicate<Transaction> TRANSACTION_TODAY =
    trans -> trans.getDate().toLocalDate().isEqual(OffsetDateTime.now(clock).toLocalDate());

  private Predicate<Transaction> TRANSACTION_FUTURE =
    trans -> trans.getDate().toLocalDate().isAfter(OffsetDateTime.now(clock).toLocalDate());

  public TransactionStatusDto apply(TransactionStatusFilterDto status, Transaction transaction) {
    TransactionStatusDto result = new TransactionStatusDto()
      .reference(status.getReference())
      .status(Status.INVALID.getValue());

    if (transaction != null) {
      if (CLIENT_OR_ATM_CHANNEL_FILTER.test(status)) {
        result = new TransactionStatusDto()
          .reference(status.getReference())
          .amount(transaction.getAmount() - transaction.getFee());
      } else if (INTERNAL_CHANNEL_FILTER.test(status)) {
        result = new TransactionStatusDto()
          .reference(status.getReference())
          .amount(transaction.getAmount())
          .fee(transaction.getFee());
      }

      if (TRANSACTION_PAST.test(transaction)) {
        result.status(Status.SETTLED.getValue());
      } else if (TRANSACTION_TODAY.test(transaction)) {
        result.status(Status.PENDING.getValue());
      } else if (TRANSACTION_FUTURE.test(transaction)) {
        if (status.getChannel().equals(Channels.ATM.getValue())) {
          result.status(Status.PENDING.getValue());
        } else {
          result.status(Status.FUTURE.getValue());
        }
      }

    }

    return result;
  }

}
