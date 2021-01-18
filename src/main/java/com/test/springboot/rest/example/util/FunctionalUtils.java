package com.test.springboot.rest.example.util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FunctionalUtils {
  public static <T> Predicate<T> not(Predicate<T> p) { return o -> !p.test(o); }

  public static <T,U> List<U> applyToList(List<T> list, Function<T,U> function) {
    return list.stream().map(function).collect(Collectors.toList());
  }
}
