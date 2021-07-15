package io.github.lambig.deferred.deferred;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExceptionsPartialApplication<E extends Exceptions> {
  private final E exceptions;

  public <V> Deferred<V, E> valueFrom(Supplier<V> valueSupplier) {
    return new Deferred<>(valueSupplier, this.exceptions);
  }

  public <V> Deferred<V, E> value(V value) {
    return new Deferred<>(() -> value, this.exceptions);
  }

  public <V> Deferred<V, E> exceptionFrom(Supplier<RuntimeException> exceptionSupplier) {
    return
        new Deferred<>(
            () -> {
              throw exceptionSupplier.get();
            },
            this.exceptions);
  }

  public <V> Deferred<V, E> exception(RuntimeException exception) {
    return exceptionFrom(() -> exception);
  }
}
