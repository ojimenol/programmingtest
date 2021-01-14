package com.test.springboot.rest.example.transaction.generator;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class TransactionRefGenerator {

  static final int CHAR_A_INDEX = 'A';
  static final int CHAR_Z_INDEX = 'Z';
  static final int REFERENCE_SIZE = 6;
  static final int REFERENCE_MAX_INT_VALUE = (int) Math.pow(10, REFERENCE_SIZE - 1);

  private Random random;

  public TransactionRefGenerator() {
    random = new Random();
  }

  public String generateReference() {
    return Optional.of(REFERENCE_MAX_INT_VALUE + 1)
      .map(random::nextInt)
      .map(Math::abs)
      .map(number -> number + calcAlpha(number))
      .map(this::fillWithZeros)
      .orElse(String.valueOf(CHAR_A_INDEX));
  }

  private String fillWithZeros(String reference) {
    return Optional.of(reference)
      .map(String::valueOf)
      .filter(ref -> ref.length() < REFERENCE_SIZE)
      .map(ref -> String.format("%1$" + REFERENCE_SIZE + "s", ref))
      .map(ref -> ref.replaceAll("\\s", "0"))
      .orElse(reference);
  }

  private String calcAlpha(int number) {
    return Optional.of(number)
      .map(value -> value % (CHAR_Z_INDEX - CHAR_A_INDEX + 1))
      .map(charOffset -> CHAR_A_INDEX + charOffset)
      .map(charIndex -> (char) charIndex.intValue())
      .map(String::valueOf)
      .orElse(String.valueOf(CHAR_A_INDEX));
  }
}
