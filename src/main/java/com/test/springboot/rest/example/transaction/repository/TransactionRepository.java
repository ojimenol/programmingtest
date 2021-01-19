package com.test.springboot.rest.example.transaction.repository;

import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Query("from Transaction t where t.reference.value = :ref")
  Optional<Transaction> findByReference(@Param("ref") String reference);

  List<Transaction> findByAccountIban(String iban);
}
