package com.test.springboot.rest.example.account.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AccountDtoTest {

  @Test
  public void testAccountDtoSettersAndGetters() {
    AccountDto dto = new AccountDto();
    dto.setIban("IBAN");
    dto.setBalance(0.0);

    assertThat(dto.getIban()).isEqualTo("IBAN");
    assertThat(dto.getBalance()).isEqualTo(0.0);
  }

  @Test
  public void testAccountFluentMethods() {
    AccountDto dto = new AccountDto()
    .iban("IBAN")
    .balance(0.0);

    assertThat(dto.getIban()).isEqualTo("IBAN");
    assertThat(dto.getBalance()).isEqualTo(0.0);
  }

}
