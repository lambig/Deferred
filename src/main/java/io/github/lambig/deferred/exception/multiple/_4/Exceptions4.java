package io.github.lambig.deferred.exception.multiple._4;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Exceptions4<A extends Exception, B extends Exception, C extends Exception, D extends Exception> implements Exceptions {
  private final Class<A> clazzA;
  private final Class<B> clazzB;
  private final Class<C> clazzC;
  private final Class<D> clazzD;

  public static <S extends Exception, T extends Exception, U extends Exception, V extends Exception> Exceptions4<S, T, U, V> of(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC, Class<V> clazzD) {
    return new Exceptions4<>(clazzA, clazzB, clazzC, clazzD);
  }

  public static <S extends Exception, T extends Exception, U extends Exception, V extends Exception> Exceptions4<S, T, U, V> exceptions4(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC, Class<V> clazzD) {
    return of(clazzA, clazzB, clazzC, clazzD);
  }
}
