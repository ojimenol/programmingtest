package com.test.springboot.rest.example.transaction.generator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TransactionRefGeneratorTest {

  private TransactionRefGenerator transactionRefGenerator;

  @Before
  public void setup() {
    transactionRefGenerator = new TransactionRefGenerator();
  }

  @Test
  public void generateRandomKey() {
    String reference = transactionRefGenerator.generateReference();

    assertThat(reference).hasSize(TransactionRefGenerator.REFERENCE_SIZE);

  }
}