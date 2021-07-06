package io.github.lambig.deferred;

import static io.github.lambig.deferred.Deferred.ifThrown;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeferredTest {

  @Nested
  class WithExplicitHandling {
    @Test
    void handled_correctly() {
      //SetUp
      Deferred<Integer, ExpectedRuntimeException> target = Deferred.valueFrom(() -> 2);
      //Exercise
      Integer actual = target.resolve(e -> e.value() + 1);
      //Verify
      assertThat(actual).isEqualTo(2);
    }

    @Test
    void handled_correctly_multiple_deferred_values() {
      //SetUp
      //Exercise
      List<Integer> actual =
          Stream.
              <Deferred<Integer, ExpectedRuntimeException>>of(
                  Deferred.valueOf(1),
                  Deferred.valueFrom(() -> 2),
                  Deferred.valueFrom(() -> {
                    throw new ExpectedExtendedRuntimeException();
                  }),
                  Deferred.exceptionFrom(ExpectedExtendedRuntimeException::new),
                  Deferred.exceptionOf(new ExpectedExtendedRuntimeException()))
              .map(ifThrown(e -> e.value() - 1))
              .collect(toList());
      //Verify
      assertThat(actual).containsExactly(1, 2, -1, -1, -1);
    }

    @Test
    void cannot_handle_unexpected_type_of_exception() {
      //SetUp
      //Exercise
      try {
        Stream.
            <Deferred<Integer, ExpectedExtendedRuntimeException>>of(
                Deferred.valueOf(1),
                Deferred.valueFrom(() -> 2),
                Deferred.valueFrom(() -> {
                  throw new ExpectedAnotherExtendedRuntimeException();
                }),
                Deferred.exceptionFrom(ExpectedExtendedRuntimeException::new),
                Deferred.exceptionOf(new ExpectedExtendedRuntimeException()))
            .map(ifThrown(e -> e.value() - 1))
            .collect(toList());
      } catch (UnexpectedExceptionThrownException e) {
        //Verify
        assertThat(e).getCause().isInstanceOf(ExpectedAnotherExtendedRuntimeException.class);
        assertThat(e).extracting(Throwable::getStackTrace).matches(Arrays::isNullOrEmpty);

      }
    }
  }

  @Nested
  class WithoutExplicitHandling {
    @Test
    void throws_the_first_error() {
      //SetUp
      Stream<Deferred<Integer, RuntimeException>> deferredValues
          =
          Stream.
              of(
                  Deferred.valueOf(1),
                  Deferred.valueFrom(() -> 2),
                  Deferred.exceptionFrom(ExpectedAnotherExtendedRuntimeException::new),
                  Deferred.exceptionOf(new ExpectedExtendedRuntimeException()));
      //Exercise
      try {
        deferredValues.map(Deferred::resolve).collect(toList());
        //Verify
        fail();
      } catch (ExpectedExtendedRuntimeException e) {
        fail();
      } catch (ExpectedAnotherExtendedRuntimeException e) {
        assertThat(e).isInstanceOf(ExpectedAnotherExtendedRuntimeException.class);
      } catch (Exception e) {
        ///
      }
    }
  }


  @Nested
  class NonRuntimeException {
    @Test
    void requires_handling() {
      //SetUp

      //Exercise
      List<Integer> actual =
          Stream.
              <Deferred<Integer, ExpectedNonRuntimeException>>of(
                  Deferred.valueOf(1),
                  Deferred.valueFrom(() -> 2),
                  Deferred.exceptionFrom(ExpectedNonRuntimeException::new),
                  Deferred.exceptionOf(new ExpectedNonRuntimeException()))
              .map(Deferred.ifThrown(e -> 0))
              .collect(toList());
      //Verify
      assertThat(actual).containsExactly(1, 2, 0, 0);
    }
  }

  private static abstract class ExpectedRuntimeException extends RuntimeException {
    abstract int value();

    ExpectedRuntimeException(String message) {
      super(message);
    }
  }

  private static class ExpectedExtendedRuntimeException extends ExpectedRuntimeException {
    @Override
    int value() {
      return 0;
    }

    ExpectedExtendedRuntimeException() {
      super("this is expected");
    }
  }

  private static class ExpectedAnotherExtendedRuntimeException extends ExpectedRuntimeException {
    @Override
    int value() {
      return 1;
    }

    ExpectedAnotherExtendedRuntimeException() {
      super("this is expected");
    }
  }

  private static class ExpectedNonRuntimeException extends Exception {
    ExpectedNonRuntimeException() {
      super("this is expected B");
    }
  }

}