package io.github.lambig.deferred.exception.multiple._3;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Exceptions3<A extends Exception, B extends Exception, C extends Exception> implements Exceptions {
  private final Class<A> clazzA;
  private final Class<B> clazzB;
  private final Class<C> clazzC;

  public static <S extends Exception, T extends Exception, U extends Exception> Exceptions3<S, T, U> of(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC) {
    return new Exceptions3<>(clazzA, clazzB, clazzC);
  }

  public static <S extends Exception, T extends Exception, U extends Exception> Exceptions3<S, T, U> exceptions3(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC) {
    return of(clazzA, clazzB, clazzC);
  }
}
