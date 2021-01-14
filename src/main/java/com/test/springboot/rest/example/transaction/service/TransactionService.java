package com.test.springboot.rest.example.transaction.service;

import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilter;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.dto.TransactionSearch;
import com.test.springboot.rest.example.transaction.dto.TransactionStatus;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Transaction createTransaction(Transaction transaction);

  @Transactional(readOnly = true)
  List<Transaction> searchTransactions(TransactionSearch searchFilter);

  @Transactional(readOnly = true)
  TransactionStatus searchTransactionStatus(TransactionStatusFilter searchFilter);
}
