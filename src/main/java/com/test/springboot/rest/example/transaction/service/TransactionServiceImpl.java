package com.test.springboot.rest.example.transaction.service;

import static com.test.springboot.rest.example.util.FunctionalUtils.*;

import com.test.springboot.rest.example.account.persistent.Account;
import com.test.springboot.rest.example.account.service.AccountServiceImpl;
import com.test.springboot.rest.example.transaction.business.rules.TransactionStatusSearchRules;
import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.defs.SortMode;
import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionSearch;
import com.test.springboot.rest.example.transaction.dto.TransactionStatus;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilter;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.generator.TransactionRefGenerator;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.repository.TransactionRepository;
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
  private TransactionStatusSearchRules transactionRules;

  @Autowired
  private TransactionRefGenerator transactionRefGenerator;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private AccountServiceImpl accountService;

  @Autowired
  private TransactionStatusSearchRules transactionStatusRules;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Transaction createTransaction(Transaction transaction) {
    generateReferenceIfNull(transaction);

    generateDateIfNull(transaction);

    checkDuplicatedReference(transaction);

    Account account = getAccount(transaction.getAccountIban());

    checkPositiveBalanceAfterOperation(transaction, account);

    return transactionRepository.save(transaction);

  }

  @Transactional(propagation = Propagation.NESTED, readOnly = true)
  @Override
  public List<Transaction> searchTransactions(TransactionSearch searchFilter) {

    Comparator<Transaction> sort = Optional.of(searchFilter)
      .map(TransactionSearch::getSort)
      .filter(SortMode.DESC.getMode()::equals)
      .map(mode -> Comparator.comparing(Transaction::getAmount).reversed())
      .orElse(Comparator.comparing(Transaction::getAmount));

    return Stream.of(searchFilter)
      .map(TransactionSearch::getAccountIban)
      .map(transactionRepository::findByAccountIban)
      .flatMap(List::stream)
      .sorted(sort)
      .collect(Collectors.toList());

  }

  @Transactional(propagation = Propagation.NESTED, readOnly = true)
  @Override
  public TransactionStatus searchTransactionStatus(TransactionStatusFilter searchFilter) {

    return Optional.of(searchFilter)
      .map(TransactionStatusFilter::getReference)
      .flatMap(transactionRepository::findByReference)
      .map(transaction -> transactionStatusRules.apply(searchFilter, transaction))
      .orElse(new TransactionStatus()
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
      .map(trans -> getNewReference())
      .ifPresent(transaction::setReference);
  }

  private void generateDateIfNull(Transaction transaction) {
    Optional.of(transaction)
      .filter(trans -> Objects.isNull(trans.getDate()))
      .map(trans -> OffsetDateTime.now())
      .ifPresent(transaction::setDate);

  }

  
  private boolean existsReference(String reference) {
    return transactionRepository.findByReference(reference).isPresent();
  }

  private String getNewReference() {
    String reference = transactionRefGenerator.generateReference();

    while (existsReference(reference)) {
      reference = transactionRefGenerator.generateReference();
    }

    return reference;
  }

  private Account getAccount(String iban) {
    return accountService.getAccount(iban)
      .orElseThrow(() -> new TransactionException(Error.ACCOUNT_NOT_FOUND));
  }

  private void checkPositiveBalanceAfterOperation(Transaction transaction, Account account) {
    // Assume the quota is absolute, not a percentage
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
