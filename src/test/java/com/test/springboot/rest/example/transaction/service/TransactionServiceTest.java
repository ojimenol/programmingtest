package com.test.springboot.rest.example.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.test.springboot.rest.example.account.persistent.Account;
import com.test.springboot.rest.example.account.service.AccountServiceImpl;
import com.test.springboot.rest.example.config.JsonConfig;
import com.test.springboot.rest.example.transaction.business.rules.TransactionStatusSearchRules;
import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.defs.SortMode;
import com.test.springboot.rest.example.transaction.defs.Status;
import com.test.springboot.rest.example.transaction.dto.TransactionSearchDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.generator.TransactionRefGenerator;
import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.repository.ReferenceRepository;
import com.test.springboot.rest.example.transaction.repository.TransactionRepository;
import com.test.springboot.rest.example.transaction.util.FixedClock;
import com.test.springboot.rest.example.transaction.util.ResourceLoader;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

  private static final String EXISTING_REFERENCE = "00000A";
  private static final String FREE_REFERENCE_1 = "00001A";
  private static final String FREE_REFERENCE_2 = "00002A";

  @InjectMocks
  private TransactionServiceImpl transactionService;

  @Spy
  private Clock clock = FixedClock.of(Instant.parse("2020-01-01T00:00:00Z"));

  @Mock
  private TransactionRefGenerator transactionRefGenerator;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountServiceImpl accountService;

  @Mock
  private TransactionStatusSearchRules transactionStatusRules;

  @Mock
  private ReferenceRepository referenceRepository;

  private Transaction transaction;
  private Account account;

  @Before
  public void setUp() {
    ResourceLoader loader = new ResourceLoader(new JsonConfig().objectMapper());
    transaction = loader.loadResourceJsonObject("transaction/service/create_transaction.json", Transaction.class);

    given(accountService.getAccountByIban(transaction.getAccount().getIban())).willReturn(Optional.of(transaction.getAccount()));

    given(referenceRepository.findByValue(FREE_REFERENCE_1)).willReturn(Optional.empty());
    given(referenceRepository.findByValue(FREE_REFERENCE_2)).willReturn(Optional.empty());
    given(referenceRepository.findByValue(EXISTING_REFERENCE)).willReturn(Optional.of(new Reference().value(EXISTING_REFERENCE)));
  }

  @Test
  public void shouldCreateTransaction() {
    Reference reference = new Reference().value(FREE_REFERENCE_1);

    transaction.setReference(reference);

    given(referenceRepository.save(reference)).willReturn(reference);
    given(transactionRepository.save(transaction)).willReturn(transaction);

    Transaction newTransaction = transactionService.createTransaction(transaction);

    assertThat(newTransaction).isNotNull();
    assertThat(newTransaction).isEqualToComparingFieldByField(transaction);
  }

  @Test
  public void shouldNotCreateTransactionByPersistenceError() {

    Transaction newTransaction = transactionService.createTransaction(transaction);

    assertThat(newTransaction).isNull();
  }

  @Test
  public void shouldCreateTransactionGeneratingReference() {
    transaction.setReference(null);

    Reference reference = new Reference().value(FREE_REFERENCE_2);

    given(referenceRepository.save(reference)).willReturn(reference);
    given(transactionRefGenerator.generateReference()).willReturn(reference);
    given(transactionRepository.save(transaction)).willReturn(transaction);

    Transaction newTransaction = transactionService.createTransaction(transaction);

    assertThat(newTransaction).isNotNull();
    assertThat(newTransaction).isEqualToComparingFieldByField(transaction);
  }

  @Test(expected = TransactionException.class)
  public void shouldNotCreateTransactionByDuplicatedReference() {

    transaction.setReference(new Reference().value(EXISTING_REFERENCE));

    transactionService.createTransaction(transaction);
  }

  @Test(expected = TransactionException.class)
  public void shouldNotCreateTransactionByNegativeBalance() {
    transaction.setAmount(-100.0);
    transaction.setReference(new Reference().value(FREE_REFERENCE_1));

    transactionService.createTransaction(transaction);
  }

  @Test
  public void shouldSearchTransactions() {
    TransactionSearchDto filter = new TransactionSearchDto()
      .accountIban(transaction.getAccount().getIban())
      .sort(SortMode.ASC.getMode());

    given(transactionRepository.findByAccountIban(filter.getAccountIban()))
      .willReturn(Collections.singletonList(transaction));

    List<Transaction> transactions = transactionService.searchTransactions(filter);

    assertThat(transactions).isNotNull();
    assertThat(transactions).isNotEmpty();
    assertThat(transactions).hasSize(1);

  }

  @Test
  public void shouldSearchTransactionsReturnEmpty() {
    TransactionSearchDto filter = new TransactionSearchDto()
      .accountIban(transaction.getAccount().getIban())
      .sort(SortMode.ASC.getMode());

    given(transactionRepository.findByAccountIban(filter.getAccountIban()))
      .willReturn(Collections.emptyList());

    List<Transaction> transactions = transactionService.searchTransactions(filter);

    assertThat(transactions).isNotNull();
    assertThat(transactions).isEmpty();

  }

  @Test
  public void shouldSearchTransactionsOrderingAscending() {

    TransactionSearchDto filter = new TransactionSearchDto()
      .accountIban(transaction.getAccount().getIban())
      .sort(SortMode.ASC.getMode());

    given(transactionRepository.findByAccountIban(filter.getAccountIban()))
      .willReturn(Arrays.asList(transaction, transaction.clone().amount(100.0), transaction.clone().amount(50.0)));

    List<Transaction> transactions = transactionService.searchTransactions(filter);

    assertThat(transactions).isNotNull();
    assertThat(transactions).isNotEmpty();
    assertThat(transactions).hasSize(3);
    assertThat(transactions).isSortedAccordingTo(Comparator.comparingDouble(Transaction::getAmount));

  }

  @Test
  public void shouldSearchTransactionsStatus() {

    TransactionStatusFilterDto searchFilter = new TransactionStatusFilterDto()
      .reference(EXISTING_REFERENCE)
      .channel(Channels.ATM.getValue());

    Reference searchReference = new Reference().value(searchFilter.getReference());

    TransactionStatusDto status  = transactionService.searchTransactionStatus(searchFilter);

    assertThat(status).isNotNull();
    assertThat(allStatus()).containsAnyOf(status.getStatus());

  }

  @Test
  public void shouldSearchTransactionsOrderingDescending() {

    TransactionSearchDto filter = new TransactionSearchDto()
      .accountIban(transaction.getAccount().getIban())
      .sort(SortMode.DESC.getMode());

    given(transactionRepository.findByAccountIban(filter.getAccountIban()))
      .willReturn(Arrays.asList(transaction, transaction.clone().amount(100.0), transaction.clone().amount(50.0)));

    List<Transaction> transactions = transactionService.searchTransactions(filter);

    assertThat(transactions).isNotNull();
    assertThat(transactions).isNotEmpty();
    assertThat(transactions).hasSize(3);
    assertThat(transactions).isSortedAccordingTo(Comparator.comparingDouble(Transaction::getAmount).reversed());

  }

  private List<String> allStatus() {
    return Stream.of(Status.values())
      .map(Status::getValue)
      .collect(Collectors.toList());
  }
}
