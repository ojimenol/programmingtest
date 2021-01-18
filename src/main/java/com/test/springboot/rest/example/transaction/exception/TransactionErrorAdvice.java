package com.test.springboot.rest.example.transaction.exception;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"com.test.springboot.rest.example.transaction.controller"})
public class TransactionErrorAdvice {

  @ExceptionHandler(TransactionException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody TransactionResponse<?> handleTransactionException(TransactionException exception, HttpServletRequest request) {

    return Optional.of(exception)
      .map(TransactionException::getCode)
      .map(Error::findErrorByCode)
      .map(error -> new TransactionResponse<Object>(error.getCode(), error.getDescription()))
      .orElseThrow(() -> new RuntimeException(Error.DEFAULT_ERROR_MESSAGE.getDescription()));

  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody TransactionResponse<?> handleGenericException(Exception exception, HttpServletRequest request) {

    return new TransactionResponse<>(Error.DEFAULT_ERROR_MESSAGE);

  }
}
