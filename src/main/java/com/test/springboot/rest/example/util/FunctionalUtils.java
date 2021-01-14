package com.test.springboot.rest.example.util;

import java.util.function.Predicate;

public class FunctionalUtils {
  public static <T> Predicate<T> not(Predicate<T> p) { return o -> !p.test(o); }
}
