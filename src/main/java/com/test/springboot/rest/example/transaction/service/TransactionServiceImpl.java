package com.test.springboot.rest.example.transaction.service;

import static com.test.springboot.rest.example.util.FunctionalUtils.*;

import com.test.springboot.rest.example.account.persistent.Account;
import com.test.springboot.rest.example.account.service.AccountService;
import com.test.springboot.rest.example.transaction.business.rules.TransactionStatusSearchRules;
import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.defs.SortMode;
import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionSearchDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.generator.TransactionRefGenerator;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.repository.TransactionRepository;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements  TransactionService {

  @Autowired
  private Clock clock;

  @Autowired
  private TransactionRefGenerator transactionRefGenerator;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private AccountService accountService;

  @Autowired
  private TransactionStatusSearchRules transactionStatusRules;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Transaction createTransaction(Transaction transaction) {
    generateReferenceIfNull(transaction);

    generateDateIfNull(transaction);

    checkDuplicatedReference(transaction);

    Account account = Optional.of(transaction)
      .map(Transaction::getAccountIban)
      .flatMap(accountService::getAccountByIban)
      .orElseThrow(() -> new TransactionException(Error.ACCOUNT_NOT_FOUND));

    checkPositiveBalanceAfter(transaction, account);

    return transactionRepository.save(transaction);

  }

  @Override
  public List<Transaction> searchTransactions(TransactionSearchDto searchFilter) {

    Comparator<Transaction> sort = Optional.of(searchFilter)
      .map(TransactionSearchDto::getSort)
      .filter(SortMode.DESC.getMode()::equals)
      .map(mode -> Comparator.comparing(Transaction::getAmount).reversed())
      .orElse(Comparator.comparing(Transaction::getAmount));

    return Stream.of(searchFilter)
      .map(TransactionSearchDto::getAccountIban)
      .map(transactionRepository::findByAccountIban)
      .flatMap(List::stream)
      .sorted(sort)
      .collect(Collectors.toList());

  }

  @Override
  public TransactionStatusDto searchTransactionStatus(TransactionStatusFilterDto searchFilter) {

    return Optional.of(searchFilter)
      .map(TransactionStatusFilterDto::getReference)
      .flatMap(transactionRepository::findByReference)
      .map(transaction -> transactionStatusRules.apply(searchFilter, transaction))
      .orElse(new TransactionStatusDto()
        .reference(searchFilter.getReference())
        .status(Status.INVALID.getValue()));

  }

  private void checkDuplicatedReference(Transaction transaction) {
    Optional.of(transaction)
      .map(Transaction::getReference)
      .filter(not(this::existsReference))
      .orElseThrow(() -> new TransactionException(Error.CREATE_TRANSACTION_REFERENCE_EXISTS));
  }

  private void generateReferenceIfNull(Transaction transaction) {
    Optional.of(transaction)
      .filter(trans -> Objects.isNull(trans.getReference()))
      .map(trans -> transactionRefGenerator.generateReference())
      .ifPresent(transaction::setReference);
  }

  private void generateDateIfNull(Transaction transaction) {
    Optional.of(transaction)
      .filter(trans -> Objects.isNull(trans.getDate()))
      .map(trans -> OffsetDateTime.now(clock))
      .ifPresent(transaction::setDate);

  }
  
  private boolean existsReference(String reference) {
    return transactionRepository.findByReference(reference).isPresent();
  }

  private void checkPositiveBalanceAfter(Transaction transaction, Account account) {
    // Assume quota is absolute, not a percentage
    double newAmount = Optional.of(transaction)
      .map(Transaction::getFee)
      .map(fee -> transaction.getAmount() - fee)
      .orElse(transaction.getAmount());


    Optional.of(account)
      .map(Account::getBalance)
      .map(balance -> balance + newAmount)
      .filter(newBalance -> newBalance > 0)
      .map(newBalance -> Boolean.TRUE)
      .orElseThrow(() -> new TransactionException(Error.CREATE_TRANSACTION_POST_BALANCE_NEGATIVE));

  }
}
