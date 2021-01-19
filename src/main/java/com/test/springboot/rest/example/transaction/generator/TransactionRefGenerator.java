package com.test.springboot.rest.example.transaction.generator;

import static com.test.springboot.rest.example.util.FunctionalUtils.peek;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.repository.ReferenceRepository;
import com.test.springboot.rest.example.util.FunctionalUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

  public Reference generateReference() {
    Reference newReference = referenceRepository.save(new Reference());

    return Optional.of(newReference)
      .map(Reference::getId)
      .map(this::getReferenceValue)
      .map(refValue -> newReference.clone().value(refValue))
      .orElse(null);
  }

  private String getReferenceValue(Long id) {
    return Optional.of(Optional.ofNullable(id).orElse(1L))
      .filter(number -> number >= 0)
      .map(number -> number % REFERENCE_NUMERIC_ELEMENTS_NUMBER + toAlpha(number / REFERENCE_NUMERIC_ELEMENTS_NUMBER))
      .map(reference -> String.format("%" + REFERENCE_SIZE + "s", reference))
      .map(reference -> reference.replaceAll(" ", "0"))
      .orElse(null);
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
