package io.github.lambig.deferred.exception.multiple._4;


import static io.github.lambig.patterns.Patterns.orElseThrow;
import static io.github.lambig.patterns.Patterns.patterns;
import static io.github.lambig.patterns.Patterns.thenApply;
import static io.github.lambig.patterns.Patterns.whenMatch;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import java.util.function.Function;

public class ExceptionsHandler4<A extends Exception, B extends Exception, C extends Exception, D extends Exception, E extends Exceptions4<A, B, C, D>, V> implements ExceptionsHandler<E, V> {

  private final Function<Exception, V> handling;

  ExceptionsHandler4(
      E exceptions,
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC,
      Function<D, V> handlingD) {
    this.handling =
        patterns(
            whenMatch(exceptions.clazzA(), thenApply(handlingA)),
            whenMatch(exceptions.clazzB(), thenApply(handlingB)),
            whenMatch(exceptions.clazzC(), thenApply(handlingC)),
            whenMatch(exceptions.clazzD(), thenApply(handlingD)),
            orElseThrow(UnexpectedExceptionThrownException::new));
  }

  @Override
  public V handle(Exception exception) {
    return this.handling.apply(exception);
  }
}
