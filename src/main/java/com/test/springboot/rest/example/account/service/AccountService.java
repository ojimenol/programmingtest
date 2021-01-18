package com.test.springboot.rest.example.account.service;

import com.test.springboot.rest.example.account.persistent.Account;
import java.util.Optional;

public interface AccountService {

  public Optional<Account> getAccount(String iban);
}
