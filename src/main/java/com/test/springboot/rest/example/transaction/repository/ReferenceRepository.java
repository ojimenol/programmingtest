package com.test.springboot.rest.example.transaction.repository;

import com.test.springboot.rest.example.transaction.persistent.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {

}
