package com.test.springboot.rest.example.account.repository;

import com.test.springboot.rest.example.account.persistent.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByIban(String iban);
}
