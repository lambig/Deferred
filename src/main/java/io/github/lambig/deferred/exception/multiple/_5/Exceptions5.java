package io.github.lambig.deferred.exception.multiple._5;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Exceptions5<
    A extends Exception,
    B extends Exception,
    C extends Exception,
    D extends Exception,
    E extends Exception
    > implements Exceptions {

  private final Class<A> clazzA;
  private final Class<B> clazzB;
  private final Class<C> clazzC;
  private final Class<D> clazzD;
  private final Class<E> clazzE;

  public static <
      S extends Exception,
      T extends Exception,
      U extends Exception,
      V extends Exception,
      W extends Exception> Exceptions5<S, T, U, V, W> of(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC, Class<V> clazzD, Class<W> clazzE) {
    return new Exceptions5<>(clazzA, clazzB, clazzC, clazzD, clazzE);
  }

  public static <
      S extends Exception,
      T extends Exception,
      U extends Exception,
      V extends Exception,
      W extends Exception> Exceptions5<S, T, U, V, W> exceptions5(Class<S> clazzA, Class<T> clazzB, Class<U> clazzC, Class<V> clazzD, Class<W> clazzE) {
    return of(clazzA, clazzB, clazzC, clazzD, clazzE);
  }
}
