package io.github.lambig.deferred.exception.multiple._1;


import static io.github.lambig.patterns.Patterns.orElseThrow;
import static io.github.lambig.patterns.Patterns.patterns;
import static io.github.lambig.patterns.Patterns.thenApply;
import static io.github.lambig.patterns.Patterns.whenMatch;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple.ExceptionsHandler;
import java.util.function.Function;

public class ExceptionHandler1<A extends Exception, E extends Exception1<A>, V> implements ExceptionsHandler<E, V> {

  private final Function<Exception, V> handler;

  ExceptionHandler1(
      E exceptions,
      Function<A, V> handler
  ) {
    this.handler =
        patterns(
            whenMatch(exceptions.clazzA(), thenApply(handler)),
            orElseThrow(UnexpectedExceptionThrownException::new));
  }

  @Override
  public V handle(Exception exception) {
    return this.handler.apply(exception);
  }

}
