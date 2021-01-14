package com.test.springboot.rest.example.util;

import java.util.function.Function;

public class Unchecked {

  @FunctionalInterface
  public interface UncheckedFunction<T, R> extends Function<T, R> {
    R applyUnsafe(T t) throws Exception;
    @Override default R apply(T t) {
      try { return applyUnsafe(t); } catch (Exception ex) {
        return throwAsUnchecked(ex);
      }
    }
  }

  public static <T, R> Function<T, R> function(UncheckedFunction<T, R> function) {
    return function;
  }

  @SuppressWarnings ("unchecked")
  static <T, E extends Throwable> T throwAsUnchecked(Throwable exception) throws E {
    throw (E) exception;
  }
}
