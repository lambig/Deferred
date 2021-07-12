package io.github.lambig.deferred.exception.multiple;

import java.util.function.Function;

public interface ExceptionsHandler<E extends Exceptions, V> extends Function<Exception, V> {
  V handle(Exception exception);

  @Override
  default V apply(Exception e) {
    return this.handle(e);
  }

}
