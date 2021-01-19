package com.test.springboot.rest.example.transaction.controller;

import static com.test.springboot.rest.example.util.FunctionalUtils.*;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.dto.TransactionDto;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusFilterDto;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.mapper.TransactionMapper;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.dto.TransactionSearchDto;
import com.test.springboot.rest.example.transaction.dto.TransactionStatusDto;
import com.test.springboot.rest.example.transaction.service.TransactionService;
import com.test.springboot.rest.example.transaction.service.TransactionServiceImpl;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class TransactionResource {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionMapper mapper;

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse<TransactionDto>> createTransaction(@Valid @RequestBody TransactionDto transaction) {

        return Optional.of(transaction)
          .map(mapper::toEntity)
          .map(transactionService::createTransaction)
          .map(mapper::toDto)
          .map(TransactionResponse::new).map(TransactionResponse::ok)
          .map(ResponseEntity.ok()::body)
          .orElseThrow(() -> new TransactionException(Error.CREATE_TRANSACTION_FAIL));

    }

    @GetMapping("/transactions/search")
    public ResponseEntity<TransactionResponse<List<TransactionDto>>> getTransactions(
      @ApiParam(value = "Transactions account iban", required = true) @RequestParam("account_iban") String iban,
      @ApiParam(value = "Sort results", allowableValues = "ASC, DESC") @RequestParam(value = "sort", required = false) String sort
    ) {

        TransactionSearchDto searchFilter = new TransactionSearchDto().accountIban(iban).sort(sort);

        return Optional.of(searchFilter)
          .map(transactionService::searchTransactions)
          .map(transactions -> applyToList(transactions, mapper::toDto))
          .map(TransactionResponse::new)
          .map(ResponseEntity::ok)
          .orElseThrow(RuntimeException::new);

    }

    @GetMapping("/transactions/status")
    public ResponseEntity<TransactionResponse<TransactionStatusDto>> getTransactionStatus(
      @ApiParam(value = "Transaction reference", required = true) @RequestParam("reference") String reference,
      @ApiParam(value = "Channel") @RequestParam(value = "channel") String channel
    ) {

        TransactionStatusFilterDto search = new TransactionStatusFilterDto()
          .reference(reference)
          .channel(channel);

        return Optional.of(search)
          .map(transactionService::searchTransactionStatus)
          .map(TransactionResponse::new)
          .map(ResponseEntity::ok)
          .orElseThrow(RuntimeException::new);

    }
}
