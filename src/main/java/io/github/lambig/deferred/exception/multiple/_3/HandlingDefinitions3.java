package io.github.lambig.deferred.exception.multiple._3;

import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HandlingDefinitions3<A extends Exception, B extends Exception, C extends Exception, V> implements HandlingDefinitions<Exceptions3<A, B, C>, V> {
  private final Function<A, V> handlingA;
  private final Function<B, V> handlingB;
  private final Function<C, V> handlingC;

  public static <A extends Exception, B extends Exception, C extends Exception, V> HandlingDefinitions3<A, B, C, V> handling(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC
  ) {
    return new HandlingDefinitions3<>(handlingA, handlingB, handlingC);
  }

  public static <A extends Exception, B extends Exception, C extends Exception, V> HandlingDefinitions3<A, B, C, V> handling3(
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC
  ) {
    return handling(handlingA, handlingB, handlingC);
  }


  @Override
  public ExceptionsHandler<Exceptions3<A, B, C>, V> withExceptions(Exceptions3<A, B, C> exceptions) {
    return new ExceptionsHandler3<>(exceptions, this.handlingA, this.handlingB, this.handlingC);
  }
}
