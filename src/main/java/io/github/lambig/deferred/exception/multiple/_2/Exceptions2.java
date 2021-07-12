package io.github.lambig.deferred.exception.multiple._2;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Exceptions2<A extends Exception, B extends Exception> implements Exceptions {
  private final Class<A> clazzA;
  private final Class<B> clazzB;

  public static <S extends Exception, T extends Exception> Exceptions2<S, T> of(Class<S> clazzA, Class<T> clazzB) {
    return new Exceptions2<>(clazzA, clazzB);
  }

  public static <S extends Exception, T extends Exception> Exceptions2<S, T> exceptions2(Class<S> clazzA, Class<T> clazzB) {
    return of(clazzA, clazzB);
  }
}
