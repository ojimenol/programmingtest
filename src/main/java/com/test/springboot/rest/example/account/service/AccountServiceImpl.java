package com.test.springboot.rest.example.account.service;

import com.test.springboot.rest.example.account.persistent.Account;
import com.test.springboot.rest.example.account.repository.AccountRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository accountRepository;

  public Optional<Account> getAccount(String iban) {

    return accountRepository.findByIban(iban);
  }

}
