package io.github.lambig.deferred.exception;

public class UnexpectedExceptionThrownException extends RuntimeException {
  public UnexpectedExceptionThrownException(Exception e) {
    super("Unexpected exception has been thrown: ", e);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
