package io.github.lambig.deferred.exception.multiple._4;

import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlingDefinitions4<A extends Exception, B extends Exception, C extends Exception, D extends Exception, V> implements HandlingDefinitions<Exceptions4<A, B, C, D>, V> {
  private final Function<A, V> handlingA;
  private final Function<B, V> handlingB;
  private final Function<C, V> handlingC;
  private final Function<D, V> handlingD;

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception, V> HandlingDefinitions4<A, B, C, D, V> handling(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD
  ) {
    return new HandlingDefinitions4<>(handlingA, handlingB, handlingC, handlingD);
  }

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception, V> HandlingDefinitions4<A, B, C, D, V> handling4(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD
  ) {
    return handling(handlingA, handlingB, handlingC, handlingD);
  }


  @Override
  public ExceptionsHandler<Exceptions4<A, B, C, D>, V> withExceptions(Exceptions4<A, B, C, D> exceptions) {
    return new ExceptionsHandler4<>(exceptions, this.handlingA, this.handlingB, this.handlingC, this.handlingD);
  }
}
