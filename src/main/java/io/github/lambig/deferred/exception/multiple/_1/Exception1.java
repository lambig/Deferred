package io.github.lambig.deferred.exception.multiple._1;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Exception1<A extends Exception> implements Exceptions {
  private final Class<A> clazzA;

  public static <S extends Exception> Exception1<S> of(Class<S> clazzA) {
    return new Exception1<>(clazzA);
  }

  public static <S extends Exception> Exception1<S> exception1(Class<S> clazzA) {
    return of(clazzA);
  }
}
