package com.test.springboot.rest.example.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.test.springboot.rest.example.account.persistent.Account;
import com.test.springboot.rest.example.account.repository.AccountRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  private static final String ACCOUNT_IBAN = "IBAN";
  private static final String ACCOUNT_IBAN_INEXISTENT = "IBAN_INEXISTENT";
  private static final Double BALANCE_ZERO = 0.0;

  @InjectMocks
  private AccountServiceImpl accountService;

  @Mock
  private AccountRepository accountRepository;

  private Account account;

  @Before
  public void setUp() {
    account = new Account().iban(ACCOUNT_IBAN).balance(BALANCE_ZERO);

    given(accountRepository.findByIban(ACCOUNT_IBAN)).willReturn(Optional.of(account));

  }

  @Test
  public void shouldGetAccountByIban() {

    Optional<Account> result = accountService.getAccountByIban(ACCOUNT_IBAN);

    assertThat(result).isNotEmpty();
    assertThat(result.get()).isEqualToComparingFieldByField(account);
  }

  @Test
  public void shouldNotGetAccountByIban() {

    Optional<Account> result = accountService.getAccountByIban(ACCOUNT_IBAN_INEXISTENT);

    assertThat(result).isEmpty();
  }
}
