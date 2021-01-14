package com.test.springboot.rest.example.transaction.repository;

import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  Optional<Transaction> findByReference(String reference);

  List<Transaction> findByAccountIban(String iban);
}
