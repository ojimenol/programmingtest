package com.test.springboot.rest.example.transaction.controller;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilter;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.dto.TransactionSearch;
import com.test.springboot.rest.example.transaction.dto.TransactionStatus;
import com.test.springboot.rest.example.transaction.service.TransactionServiceImpl;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@RestController
public class TransactionResource {

    @Autowired
    TransactionServiceImpl transactionService;

    @PostMapping("/transactions")
    public CompletionStage<ResponseEntity<TransactionResponse<Transaction>>> createTransaction(@Valid @RequestBody Transaction transaction) {

        return CompletableFuture.supplyAsync(() -> transactionService.createTransaction(transaction))
          .thenApply(newTransaction -> Optional.of(newTransaction)
            .map(TransactionResponse::new)
            .map(TransactionResponse::ok)
            .map(ResponseEntity.ok()::body)
            .orElseThrow(() -> new TransactionException(Error.CREATE_TRANSACTION_FAIL))
          )
          .exceptionally(throwable -> Optional.of(throwable)
            .map(this::findError)
            .map(error -> new TransactionResponse<Transaction>(error.getCode(), error.getDescription()))
            .map(ResponseEntity.badRequest()::body)
            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TransactionResponse<Transaction>())));
    }

    @PostMapping("/transactions/search")
    public CompletionStage<ResponseEntity<TransactionResponse<List<Transaction>>>> getTransactions(@Valid @RequestBody TransactionSearch search) {

        return CompletableFuture.supplyAsync(() -> transactionService.searchTransactions(search))
          .thenApply(TransactionResponse::new)
          .thenApply(ResponseEntity::ok)
          .exceptionally(throwable -> Optional.of(throwable)
            .map(this::findError)
            .map(error -> new TransactionResponse<List<Transaction>>(error.getCode(), error.getDescription()))
            .map(ResponseEntity.badRequest()::body)
            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TransactionResponse<List<Transaction>>())));

    }

    @PostMapping("/transactions/status")
    public CompletionStage<ResponseEntity<TransactionResponse<TransactionStatus>>> getTransactionStatus(@Valid @RequestBody TransactionStatusFilter search) {

        return CompletableFuture.supplyAsync(() -> transactionService.searchTransactionStatus(search))
          .thenApply(TransactionResponse::new)
          .thenApply(ResponseEntity::ok)
          .exceptionally(throwable -> Optional.of(throwable)
            .map(this::findError)
            .map(error -> new TransactionResponse<TransactionStatus>(error.getCode(), error.getDescription()))
            .map(ResponseEntity.badRequest()::body)
            .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TransactionResponse<TransactionStatus>())));

    }

    private Error findError(Throwable exception) {
        return Optional.of(exception)
          .map(Throwable::getCause)
          .filter(ex -> ex.getClass().isAssignableFrom(TransactionException.class))
          .map(TransactionException.class::cast)
          .map(TransactionException::getCode)
          .map(this::findErrorByCode)
          .orElse(Error.DEFAULT_ERROR_MESSAGE);

    }

    private Error findErrorByCode(String code) {
        return Stream.of(Error.values())
          .filter(error -> error.getCode().equals(code))
          .findFirst()
          .orElse(null);

    }
}
