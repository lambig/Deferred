package io.github.lambig.deferred;

import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Deferred<V, E extends Exception> {
  private final Supplier<V> valueSupplier;
  private final Supplier<E> exceptionSupplier;

  public static <V, E extends Exception> Deferred<V, E> valueFrom(Supplier<V> toBeDeferred) {
    return new Deferred<>(toBeDeferred, () -> null);
  }

  public static <V, E extends Exception> Deferred<V, E> valueOf(V toBeDeferred) {
    return new Deferred<>(() -> toBeDeferred, () -> null);
  }

  public static <V, E extends Exception> Deferred<V, E> exceptionFrom(Supplier<E> toBeDeferred) {
    return new Deferred<>(() -> null, toBeDeferred);
  }

  public static <V, E extends Exception> Deferred<V, E> exceptionOf(E toBeDeferred) {
    return new Deferred<>(() -> null, () -> toBeDeferred);
  }

  public static <V, E extends Exception> Function<Deferred<V, E>, V> ifThrown(Function<E, V> errorHandler) {
    return deferred -> deferred.resolve(errorHandler);
  }

  public V resolve() throws E {
    Exception exception = this.exceptionSupplier.get();
    if (Objects.isNull(exception)) {
      return this.valueSupplier.get();
    }
    throw this.exceptionSupplier.get();
  }

  @SuppressWarnings("unchecked")
  public V resolve(Function<E, V> errorHandler) {
    try {
      return this.resolve();
    } catch (Exception e) {
      try {
        return errorHandler.apply((E) e);
      } catch (ClassCastException e2) {
        throw new UnexpectedExceptionThrownException(e);
      }
    }
  }

}
