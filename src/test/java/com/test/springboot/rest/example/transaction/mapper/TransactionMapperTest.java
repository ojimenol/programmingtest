package com.test.springboot.rest.example.transaction.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.springboot.rest.example.transaction.dto.TransactionDto;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TransactionMapperTest {

  private TransactionMapper mapper = new TransactionMapper();

  @Test
  public void testMapDtoToEntity() {
    Transaction entity = new Transaction()
      .accountIban("IBAN")
      .reference("REFERENCE")
      .amount(0.0)
      .date(OffsetDateTime.now())
      .fee(0.0)
      .description("DESCRIPTION");

    TransactionDto dto = mapper.toDto(entity);

    assertThat(dto).isEqualToComparingFieldByField(entity);
  }

  @Test
  public void testMapEntityToDto() {
    TransactionDto dto = new TransactionDto()
      .accountIban("IBAN")
      .reference("REFERENCE")
      .amount(0.0)
      .date(OffsetDateTime.now())
      .fee(0.0)
      .description("DESCRIPTION");


    Transaction entity = mapper.toEntity(dto);

    assertThat(dto).isEqualToComparingFieldByField(entity);
  }
}
