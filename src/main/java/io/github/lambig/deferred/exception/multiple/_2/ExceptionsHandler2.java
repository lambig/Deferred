package io.github.lambig.deferred.exception.multiple._2;


import static io.github.lambig.patterns.Patterns.orElseThrow;
import static io.github.lambig.patterns.Patterns.patterns;
import static io.github.lambig.patterns.Patterns.thenApply;
import static io.github.lambig.patterns.Patterns.whenMatch;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import java.util.function.Function;

public class ExceptionsHandler2<A extends Exception, B extends Exception, E extends Exceptions2<A, B>, V> implements ExceptionsHandler<E, V> {

  private final Function<Exception, V> handler;

  ExceptionsHandler2(
      E exceptions,
      Function<A, V> handlerA,
      Function<B, V> handlerB
  ) {
    this.handler =
        patterns(
            whenMatch(exceptions.clazzA(), thenApply(handlerA)),
            whenMatch(exceptions.clazzB(), thenApply(handlerB)),
            orElseThrow(UnexpectedExceptionThrownException::new));
  }

  @Override
  public V handle(Exception exception) {
    return this.handler.apply(exception);
  }

}
