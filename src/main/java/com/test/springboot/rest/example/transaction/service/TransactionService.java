package com.test.springboot.rest.example.transaction.service;

import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.dto.TransactionSearchDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  Transaction createTransaction(Transaction transaction);

  @Transactional(readOnly = true)
  List<Transaction> searchTransactions(TransactionSearchDto searchFilter);

  @Transactional(readOnly = true)
  TransactionStatusDto searchTransactionStatus(TransactionStatusFilterDto searchFilter);
}
