package io.github.lambig.deferred.exception.multiple._5;

import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlingDefinitions5<A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exception, V> implements HandlingDefinitions<Exceptions5<A, B, C, D, E>, V> {
  private final Function<A, V> handlingA;
  private final Function<B, V> handlingB;
  private final Function<C, V> handlingC;
  private final Function<D, V> handlingD;
  private final Function<E, V> handlingE;

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exception, V> HandlingDefinitions5<A, B, C, D, E, V> handling(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD,
      Function<E, V> handlingE
  ) {
    return new HandlingDefinitions5<>(handlingA, handlingB, handlingC, handlingD, handlingE);
  }

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exception, V> HandlingDefinitions5<A, B, C, D, E, V> handling5(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD,
      Function<E, V> handlingE
  ) {
    return handling(handlingA, handlingB, handlingC, handlingD, handlingE);
  }


  @Override
  public ExceptionsHandler<Exceptions5<A, B, C, D, E>, V> withExceptions(Exceptions5<A, B, C, D, E> exceptions) {
    return new ExceptionsHandler5<>(exceptions, this.handlingA, this.handlingB, this.handlingC, this.handlingD, this.handlingE);
  }
}
