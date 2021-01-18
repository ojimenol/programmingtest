package com.test.springboot.rest.example.transaction.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(JUnit4.class)
public class TransactionErrorAdviceTest {

  private TransactionErrorAdvice advice = new TransactionErrorAdvice();

  @Test
  public void testTransactionExceptionWithCode() {

    TransactionResponse<?> response = advice.handleTransactionException(
      new TransactionException(Error.CREATE_TRANSACTION_FAIL),
      new MockHttpServletRequest());

    assertThat(response.getErrorCode()).isEqualTo(Error.CREATE_TRANSACTION_FAIL.getCode());
    assertThat(response.getErrorDescription()).isEqualTo(Error.CREATE_TRANSACTION_FAIL.getDescription());
  }

  @Test
  public void testTransactionExceptionUnknownCode() {

    TransactionResponse<?> response = advice.handleTransactionException(
      new TransactionException("", ""),
      new MockHttpServletRequest());

    assertThat(response.getErrorCode()).isEqualTo(Error.DEFAULT_ERROR_MESSAGE.getCode());
    assertThat(response.getErrorDescription()).isEqualTo(Error.DEFAULT_ERROR_MESSAGE.getDescription());
  }

  @Test
  public void testTransactionOtherException() {

    TransactionResponse<?> response = advice.handleGenericException(
      new Exception("any error description"),
      new MockHttpServletRequest());

    assertThat(response.getErrorCode()).isEqualTo(Error.DEFAULT_ERROR_MESSAGE.getCode());
    assertThat(response.getErrorDescription()).isEqualTo("any error description");
  }
}
