package com.test.springboot.rest.example.transaction.repository;

import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {

  @Query(value = "select max(id) from reference_seq", nativeQuery = true)
  Optional<Long> getNextId();

  Optional<Reference> findByValue(String value);
}
