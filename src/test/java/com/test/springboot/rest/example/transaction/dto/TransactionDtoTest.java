package com.test.springboot.rest.example.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransactionDtoTest {

  private static final String REFERENCE = "REF";
  private static final String TRANSACTION_DESCRIPTION = "TRANS_DESC";
  private static final String IBAN = "IBAN";
  private static final Double ZERO = 0.0;
  private static final Double ONE = 1.0;

  @Test
  public void testTransactionDtoSettersAndGetters() {
    TransactionDto dto = new TransactionDto();
    dto.setReference(REFERENCE);
    dto.setDescription(TRANSACTION_DESCRIPTION);
    dto.setAccountIban(IBAN);
    dto.setAmount(ZERO);
    dto.setFee(ONE);


    assertThat(dto.getReference()).isEqualTo(REFERENCE);
    assertThat(dto.getDescription()).isEqualTo(TRANSACTION_DESCRIPTION);
    assertThat(dto.getAccountIban()).isEqualTo(IBAN);
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);

  }

  @Test
  public void testTransactionDtoFluentMethods() {
    TransactionDto dto = new TransactionDto()
    .reference(REFERENCE)
    .description(TRANSACTION_DESCRIPTION)
    .accountIban(IBAN)
    .amount(ZERO)
    .fee(ONE);


    assertThat(dto.getReference()).isEqualTo(REFERENCE);
    assertThat(dto.getDescription()).isEqualTo(TRANSACTION_DESCRIPTION);
    assertThat(dto.getAccountIban()).isEqualTo(IBAN);
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);
  }

  @Test
  public void testCloneDto() {
    TransactionDto dto = new TransactionDto()
      .reference(REFERENCE)
      .description(TRANSACTION_DESCRIPTION)
      .accountIban(IBAN)
      .amount(ZERO)
      .fee(ONE);

    dto = dto.clone();

    assertThat(dto.getReference()).isEqualTo(REFERENCE);
    assertThat(dto.getDescription()).isEqualTo(TRANSACTION_DESCRIPTION);
    assertThat(dto.getAccountIban()).isEqualTo(IBAN);
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);
  }


}

