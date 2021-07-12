package io.github.lambig.deferred.deferred;

import static io.github.lambig.deferred.exception.multiple._1.Exception1.exception1;
import static io.github.lambig.deferred.exception.multiple._2.Exceptions2.exceptions2;
import static io.github.lambig.deferred.exception.multiple._3.Exceptions3.exceptions3;
import static io.github.lambig.deferred.exception.multiple._4.Exceptions4.exceptions4;
import static io.github.lambig.deferred.exception.multiple._5.Exceptions5.exceptions5;


import io.github.lambig.deferred.exception.multiple.Exceptions;
import io.github.lambig.deferred.exception.multiple.HandlingDefinitions;
import io.github.lambig.deferred.exception.multiple._1.Exception1;
import io.github.lambig.deferred.exception.multiple._2.Exceptions2;
import io.github.lambig.deferred.exception.multiple._3.Exceptions3;
import io.github.lambig.deferred.exception.multiple._4.Exceptions4;
import io.github.lambig.deferred.exception.multiple._5.Exceptions5;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Deferred<V, E extends Exceptions> {
  private final Supplier<V> valueSupplier;
  private final E exceptions;

  public static <X> ValueSupplierPartialApplication<X> valueFrom(Supplier<X> valueSupplier) {
    return new ValueSupplierPartialApplication<>(valueSupplier);
  }

  public static <E extends Exceptions> ExceptionsPartialApplication<E> possiblyThrow(E exceptions) {
    return new ExceptionsPartialApplication<>(exceptions);
  }

  public static <A extends Exception> ExceptionsPartialApplication<Exception1<A>> possiblyThrow(
      Class<A> clazzA) {
    return new ExceptionsPartialApplication<>(exception1(clazzA));
  }

  public static <A extends Exception, B extends Exception> ExceptionsPartialApplication<Exceptions2<A, B>> possiblyThrow(
      Class<A> clazzA,
      Class<B> clazzB) {
    return new ExceptionsPartialApplication<>(exceptions2(clazzA, clazzB));
  }

  public static <A extends Exception, B extends Exception, C extends Exception> ExceptionsPartialApplication<Exceptions3<A, B, C>> possiblyThrow(
      Class<A> clazzA,
      Class<B> clazzB,
      Class<C> clazzC) {
    return new ExceptionsPartialApplication<>(exceptions3(clazzA, clazzB, clazzC));
  }

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception> ExceptionsPartialApplication<Exceptions4<A, B, C, D>> possiblyThrow(
      Class<A> clazzA,
      Class<B> clazzB,
      Class<C> clazzC,
      Class<D> clazzD) {
    return new ExceptionsPartialApplication<>(exceptions4(clazzA, clazzB, clazzC, clazzD));
  }

  public static <A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exception> ExceptionsPartialApplication<Exceptions5<A, B, C, D, E>> possiblyThrow(
      Class<A> clazzA,
      Class<B> clazzB,
      Class<C> clazzC,
      Class<D> clazzD,
      Class<E> clazzE) {
    return new ExceptionsPartialApplication<>(exceptions5(clazzA, clazzB, clazzC, clazzD, clazzE));
  }

  public static <V, E extends Exceptions> Function<Deferred<V, E>, V> resolveWith(HandlingDefinitions<E, V> handling) {
    return deferred -> deferred.resolve(handling);
  }

  public V resolve() {
    return this.valueSupplier.get();
  }

  public Optional<V> resolveToOptional() {
    return Optional.ofNullable(this.resolve());
  }

  public V resolve(HandlingDefinitions<E, V> handlingDefinitions) {
    try {
      return this.valueSupplier.get();
    } catch (Exception e) {
      return handlingDefinitions.apply(this.exceptions).apply(e);
    }
  }

  public Optional<V> resolveToOptional(HandlingDefinitions<E, V> handlingDefinitions) {
    return Optional.ofNullable(this.resolve(handlingDefinitions));
  }

}
