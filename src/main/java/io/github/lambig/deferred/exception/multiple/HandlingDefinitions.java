package io.github.lambig.deferred.exception.multiple;

import java.util.function.Function;

public interface HandlingDefinitions<E extends Exceptions, V> extends Function<E, ExceptionsHandler<E, V>> {
  ExceptionsHandler<E, V> withExceptions(E exceptions);

  @Override
  default ExceptionsHandler<E, V> apply(E exceptions) {
    return this.withExceptions(exceptions);
  }
}
