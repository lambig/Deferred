package io.github.lambig.deferred.exception.multiple._1;

import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HandlingDefinition1<A extends Exception, V> implements HandlingDefinitions<Exception1<A>, V> {
  private final Function<A, V> handlingA;

  public static <A extends Exception, V> HandlingDefinition1<A, V> handling(
      Function<A, V> handling
  ) {
    return new HandlingDefinition1<>(handling);
  }

  public static <A extends Exception, V> HandlingDefinition1<A, V> handling1(
      Function<A, V> handling
  ) {
    return handling(handling);
  }

  @Override
  public ExceptionsHandler<Exception1<A>, V> withExceptions(Exception1<A> exceptions) {
    return new ExceptionHandler1<>(exceptions, this.handlingA);
  }
}
