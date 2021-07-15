package io.github.lambig.deferred.deferred;

import io.github.lambig.deferred.exception.multiple.Exceptions;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValueSupplierPartialApplication<X> {
  private final Supplier<X> valueSupplier;

  public <E extends Exceptions> Deferred<X, E> possiblyThrow(E exceptions) {
    return new Deferred<>(this.valueSupplier, exceptions);
  }
}
