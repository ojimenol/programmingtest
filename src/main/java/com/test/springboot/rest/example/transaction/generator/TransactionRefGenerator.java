package com.test.springboot.rest.example.transaction.generator;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.repository.ReferenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionRefGenerator {

  static final int CHAR_A_INDEX = 'A';
  static final int CHAR_Z_INDEX = 'Z';
  static final int REFERENCE_SIZE = 6;
  static final int REFERENCE_NUMERIC_ELEMENTS_NUMBER = 100000;

  private ReferenceRepository referenceRepository;

  public TransactionRefGenerator(ReferenceRepository repository) {
    this.referenceRepository = repository;
  }

  public String generateReference() {
    return Optional.of(new Reference())
      .map(referenceRepository::save)
      .map(this::calcReferenceValue)
      .map(referenceRepository::save)
      .map(Reference::getValue)
      .orElse(null);
  }

  private Reference calcReferenceValue(Reference ref) {
    Optional.of(ref.getId())
      .filter(number -> number >= 0)
      .map(number -> number % REFERENCE_NUMERIC_ELEMENTS_NUMBER + toAlpha(number / REFERENCE_NUMERIC_ELEMENTS_NUMBER))
      .map(reference -> String.format("%" + REFERENCE_SIZE + "s", reference))
      .map(reference -> reference.replaceAll(" ", "0"))
      .ifPresent(ref::setValue);

    return ref;
  }

  private String toAlpha(long offset) {
    return Optional.of(offset)
      .filter(charOffset -> charOffset <= CHAR_Z_INDEX - CHAR_A_INDEX)
      .map(charOffset -> CHAR_A_INDEX + charOffset)
      .map(charIndex -> (char) charIndex.intValue())
      .map(String::valueOf)
      .orElseThrow(() -> new TransactionException(Error.GENERATE_TRANSACTION_REFERENCES_FULL));
  }
}
