package com.test.springboot.rest.example.transaction.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.defs.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransactionStatusDtoTest {

  private static final String REFERENCE = "REF";
  private static final Double ZERO = 0.0;
  private static final Double ONE = 1.0;

  @Test
  public void testTransactionStatusDtoSettersAndGetters() {
    TransactionStatusDto dto = new TransactionStatusDto();
    dto.setReference(REFERENCE);
    dto.setStatus(Status.INVALID.getValue());
    dto.setChannel(Channels.INTERNAL.getValue());
    dto.setAmount(ZERO);
    dto.setFee(ONE);


    assertThat(dto.getStatus()).isEqualTo(Status.INVALID.getValue());
    assertThat(dto.getChannel()).isEqualTo(Channels.INTERNAL.getValue());
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);
    assertThat(dto.getReference()).isEqualTo(REFERENCE);
  }

  @Test
  public void testTransactionDtoFluentMethods() {
    TransactionStatusDto dto = new TransactionStatusDto()
    .status(Status.INVALID.getValue())
    .channel(Channels.INTERNAL.getValue())
    .amount(ZERO)
    .fee(ONE)
    .reference(REFERENCE);

    assertThat(dto.getStatus()).isEqualTo(Status.INVALID.getValue());
    assertThat(dto.getChannel()).isEqualTo(Channels.INTERNAL.getValue());
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);
    assertThat(dto.getReference()).isEqualTo(REFERENCE);
  }

  @Test
  public void testCloneDto() {
    TransactionStatusDto dto = new TransactionStatusDto()
      .status(Status.INVALID.getValue())
      .channel(Channels.INTERNAL.getValue())
      .amount(ZERO)
      .fee(ONE)
      .reference(REFERENCE);

    dto = dto.clone();

    assertThat(dto.getStatus()).isEqualTo(Status.INVALID.getValue());
    assertThat(dto.getChannel()).isEqualTo(Channels.INTERNAL.getValue());
    assertThat(dto.getAmount()).isEqualTo(ZERO);
    assertThat(dto.getFee()).isEqualTo(ONE);
    assertThat(dto.getReference()).isEqualTo(REFERENCE);
  }


}

