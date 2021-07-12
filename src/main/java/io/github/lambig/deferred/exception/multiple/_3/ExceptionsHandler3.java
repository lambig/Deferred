package io.github.lambig.deferred.exception.multiple._3;


import static io.github.lambig.patterns.Patterns.orElseThrow;
import static io.github.lambig.patterns.Patterns.patterns;
import static io.github.lambig.patterns.Patterns.thenApply;
import static io.github.lambig.patterns.Patterns.whenMatch;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import java.util.function.Function;

public class ExceptionsHandler3<A extends Exception, B extends Exception, C extends Exception, E extends Exceptions3<A, B, C>, V> implements ExceptionsHandler<E, V> {

  private final Function<Exception, V> handling;

  ExceptionsHandler3(
      E exceptions,
      Function<A, V> handlingA,
      Function<B, V> handlingB,
      Function<C, V> handlingC) {
    this.handling =
        patterns(
            whenMatch(exceptions.clazzA(), thenApply(handlingA)),
            whenMatch(exceptions.clazzB(), thenApply(handlingB)),
            whenMatch(exceptions.clazzC(), thenApply(handlingC)),
            orElseThrow(UnexpectedExceptionThrownException::new));
  }

  @Override
  public V handle(Exception exception) {
    return this.handling.apply(exception);
  }
}
