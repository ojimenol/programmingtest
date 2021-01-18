package com.test.springboot.rest.example.transaction.mapper;

import com.test.springboot.rest.example.transaction.dto.TransactionDto;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

  public Transaction toEntity(TransactionDto dto) {

    Objects.requireNonNull(dto, "");

    return new Transaction()
      .accountIban(dto.getAccountIban())
      .reference(dto.getReference())
      .date(dto.getDate())
      .amount(dto.getAmount())
      .fee(dto.getFee())
      .description(dto.getDescription());
  }

  public TransactionDto toDto(Transaction entity) {

    Objects.requireNonNull(entity);

    return new TransactionDto()
      .accountIban(entity.getAccountIban())
      .reference(entity.getReference())
      .date(entity.getDate())
      .amount(entity.getAmount())
      .fee(entity.getFee())
      .description(entity.getDescription());
  }
}
