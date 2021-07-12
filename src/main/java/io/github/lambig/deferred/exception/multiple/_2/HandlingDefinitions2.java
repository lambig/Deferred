package io.github.lambig.deferred.exception.multiple._2;

import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HandlingDefinitions2<A extends Exception, B extends Exception, V> implements HandlingDefinitions<Exceptions2<A, B>, V> {
  private final Function<A, V> handlingA;
  private final Function<B, V> handlingB;

  public static <A extends Exception, B extends Exception, V> HandlingDefinitions2<A, B, V> handling(
      Function<A, V> handlerA,
      Function<B, V> handlerB
  ) {
    return new HandlingDefinitions2<>(handlerA, handlerB);
  }

  public static <A extends Exception, B extends Exception, V> HandlingDefinitions2<A, B, V> handling2(
      Function<A, V> handlerA,
      Function<B, V> handlerB
  ) {
    return handling(handlerA, handlerB);
  }

  @Override
  public ExceptionsHandler<Exceptions2<A, B>, V> withExceptions(Exceptions2<A, B> exceptions) {
    return new ExceptionsHandler2<>(exceptions, this.handlingA, this.handlingB);
  }
}
