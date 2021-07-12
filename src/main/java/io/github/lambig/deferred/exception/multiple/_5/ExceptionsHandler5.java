package io.github.lambig.deferred.exception.multiple._5;


import static io.github.lambig.patterns.Patterns.orElseThrow;
import static io.github.lambig.patterns.Patterns.patterns;
import static io.github.lambig.patterns.Patterns.thenApply;
import static io.github.lambig.patterns.Patterns.whenMatch;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import java.util.function.Function;

public class ExceptionsHandler5<A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exception, X extends Exceptions5<A, B, C, D, E>, V> implements ExceptionsHandler<X, V> {

  private final Function<Exception, V> handling;

  ExceptionsHandler5(
      X exceptions,
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD,
      Function<E, V> handlingE) {
    this.handling =
        patterns(
            whenMatch(exceptions.clazzA(), thenApply(handlingA)),
            whenMatch(exceptions.clazzB(), thenApply(handlingB)),
            whenMatch(exceptions.clazzC(), thenApply(handlingC)),
            whenMatch(exceptions.clazzD(), thenApply(handlingD)),
            whenMatch(exceptions.clazzE(), thenApply(handlingE)),
            orElseThrow(UnexpectedExceptionThrownException::new));
  }

  @Override
  public V handle(Exception exception) {
    return this.handling.apply(exception);
  }
}
