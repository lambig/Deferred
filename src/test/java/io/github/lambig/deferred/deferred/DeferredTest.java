package io.github.lambig.deferred.deferred;

import static io.github.lambig.deferred.deferred.Deferred.resolveWith;
import static io.github.lambig.deferred.exception.multiple._1.HandlingDefinition1.handling1;
import static io.github.lambig.deferred.exception.multiple._2.HandlingDefinitions2.handling2;
import static io.github.lambig.deferred.exception.multiple._3.Exceptions3.exceptions3;
import static io.github.lambig.deferred.exception.multiple._3.HandlingDefinitions3.handling3;
import static io.github.lambig.deferred.exception.multiple._4.HandlingDefinitions4.handling4;
import static io.github.lambig.deferred.exception.multiple._5.HandlingDefinitions5.handling5;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;


import io.github.lambig.deferred.exception.UnexpectedExceptionThrownException;
import io.github.lambig.deferred.exception.multiple._1.Exception1;
import io.github.lambig.deferred.exception.multiple._2.Exceptions2;
import io.github.lambig.deferred.exception.multiple._3.Exceptions3;
import io.github.lambig.deferred.exception.multiple._4.Exceptions4;
import io.github.lambig.deferred.exception.multiple._5.Exceptions5;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeferredTest {
  @Nested
  class WithExplicitHandling {
    @Test
    void handled_correctly_no_exception_thrown() {
      //SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1>>
          target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .valueFrom(() -> 2);
      //Exercise
      Optional<Integer> actual =
          target
              .resolveToOptional(
                  handling2(
                      expectedExtended -> 4,
                      expectedAnother -> 5));
      //Verify
      assertThat(actual).hasValue(2);
    }

    @Test
    void handled_correctly_with_an_expected_exception_thrown() {
      //SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedNonRuntimeException>>
          target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedNonRuntimeException.class)
              .valueFrom(() -> {
                throw new ExpectedExtendedRuntimeException1();
              });
      //Exercise
      Optional<Integer> actual =
          target
              .resolveToOptional(
                  handling3(
                      expectedExtended -> 4,
                      expectedAnother -> 5,
                      nonRuntime -> 6));
      //Verify
      assertThat(actual).hasValue(5);
    }

    @Test
    void handled_correctly_with_an_expected_exception_thrown_reversed() {
      //SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedRuntimeException>>
          target =
          Deferred
              .<Integer>valueFrom(() -> {
                throw new ExpectedExtendedRuntimeException1();
              })
              .possiblyThrow(
                  exceptions3(
                      ExpectedExtendedRuntimeException0.class,
                      ExpectedExtendedRuntimeException1.class,
                      ExpectedRuntimeException.class));
      //Exercise
      Optional<Integer> actual =
          target
              .resolveToOptional(
                  handling3(
                      ExpectedRuntimeException::value,
                      expectedAnother -> 5,
                      nonRuntime -> 6));
      //Verify
      assertThat(actual).hasValue(5);
    }

    @Test
    void handled_correctly_with_an_expected_exception_supplied() {
      //SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedNonRuntimeException>>
          target =
          Deferred
              .possiblyThrow(
                  exceptions3(
                      ExpectedExtendedRuntimeException0.class,
                      ExpectedExtendedRuntimeException1.class,
                      ExpectedNonRuntimeException.class))
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      //Exercise
      Optional<Integer> actual =
          target
              .resolveToOptional(
                  handling3(
                      expectedExtended -> 4,
                      expectedAnother -> 5,
                      nonRuntime -> 6));
      //Verify
      assertThat(actual).hasValue(4);
    }
  }

  @Nested
  class WithoutExplicitHandling {
    @Test
    void handled_correctly_no_exception_thrown() {
      //SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1>>
          target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .value(2);
      //Exercise
      Optional<Integer> actual = target.resolveToOptional();
      //Verify
      assertThat(actual).hasValue(2);
    }

    @Test
    void throw_a_supplied_exception() {
      //SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1>>
          target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .exceptionFrom(ExpectedExtendedRuntimeException1::new);
      //Exercise
      try {
        Optional<Integer> actual = target.resolveToOptional();
        //Verify
        fail();
      } catch (ExpectedExtendedRuntimeException1 e) {
        assertThat(e)
            .extracting(ExpectedRuntimeException.class::cast)
            .extracting(ExpectedRuntimeException::value).isEqualTo(1);
      }
    }
  }

  @Nested
  class Exception_1 {
    @Test
    void returning_value() {
      // SetUp
      Deferred<
          Integer,
          Exception1<ExpectedRuntimeException>> target =
          Deferred
              .possiblyThrow(ExpectedRuntimeException.class)
              .valueFrom(() -> 1);
      // Exercise
      Integer actual = target.resolve(handling1(ExpectedRuntimeException::value));
      // Verify
      assertThat(actual).isEqualTo(1);
    }

    @Test
    void throwing_1() {
      // SetUp
      Deferred<
          Integer,
          Exception1<ExpectedRuntimeException>> target =
          Deferred
              .possiblyThrow(ExpectedRuntimeException.class)
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      // Exercise
      Integer actual = target.resolve(handling1(ExpectedRuntimeException::value));
      // Verify
      assertThat(actual).isEqualTo(0);
    }

    @Test
    void throwing_unexpected() {
      // SetUp
      Deferred<
          Integer,
          Exception1<ExpectedRuntimeException>> target =
          Deferred
              .possiblyThrow(ExpectedRuntimeException.class)
              .exceptionFrom(NullPointerException::new);
      // Exercise
      try {
        Integer actual = target.resolve(handling1(ExpectedRuntimeException::value));
        // Verify
        fail();
      } catch (UnexpectedExceptionThrownException e) {
        assertThat(e).hasCauseInstanceOf(NullPointerException.class);
      }
    }
  }

  @Nested
  class Exceptions_2 {
    @Test
    void returning_value() {
      // SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .valueFrom(() -> 1);
      // Exercise
      Integer actual =
          target.resolve(
              handling2(
                  e -> e.value() + 10,
                  e -> e.value() + 11));
      // Verify
      assertThat(actual).isEqualTo(1);
    }

    @Test
    void throwing_1() {
      // SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling2(
                  e -> e.value() + 10,
                  e -> e.value() + 20));
      // Verify
      assertThat(actual).isEqualTo(10);
    }

    @Test
    void throwing_2() {
      // SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .exceptionFrom(ExpectedExtendedRuntimeException1::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling2(
                  e -> e.value() + 10,
                  e -> e.value() + 20));
      // Verify
      assertThat(actual).isEqualTo(21);
    }

    @Test
    void throwing_unexpected() {
      // SetUp
      Deferred<
          Integer,
          Exceptions2<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class)
              .exceptionFrom(NullPointerException::new);
      // Exercise
      try {
        Integer actual =
            target.resolve(
                handling2(
                    e -> e.value() + 10,
                    e -> e.value() + 20));
        // Verify
        fail();
      } catch (UnexpectedExceptionThrownException e) {
        assertThat(e).hasCauseInstanceOf(NullPointerException.class);
      }
    }
  }

  @Nested
  class Exceptions_3 {
    @Test
    void returning_value() {
      // SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class)
              .valueFrom(() -> 1);
      // Exercise
      Integer actual =
          target.resolve(
              handling3(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30));
      // Verify
      assertThat(actual).isEqualTo(1);
    }

    @Test
    void throwing_1() {
      // SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class)
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling3(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30));
      // Verify
      assertThat(actual).isEqualTo(10);
    }

    @Test
    void throwing_2() {
      // SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class)
              .exceptionFrom(ExpectedExtendedRuntimeException1::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling3(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30));
      // Verify
      assertThat(actual).isEqualTo(21);
    }

    @Test
    void throwing_3() {
      // SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class)
              .exceptionFrom(ExpectedExtendedRuntimeException2::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling3(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30));
      // Verify
      assertThat(actual).isEqualTo(32);
    }


    @Test
    void throwing_unexpected() {
      // SetUp
      Deferred<
          Integer,
          Exceptions3<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class)
              .exceptionFrom(NullPointerException::new);
      // Exercise
      try {
        Integer actual =
            target.resolve(
                handling3(
                    e -> e.value() + 10,
                    e -> e.value() + 20,
                    e -> e.value() + 30));
        // Verify
        fail();
      } catch (UnexpectedExceptionThrownException e) {
        assertThat(e).hasCauseInstanceOf(NullPointerException.class);
      }
    }
  }

  @Nested
  class Exceptions_4 {
    @Test
    void returning_value() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .valueFrom(() -> 1);
      // Exercise
      Integer actual =
          target.resolve(
              handling4(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40));
      // Verify
      assertThat(actual).isEqualTo(1);
    }

    @Test
    void throwing_1() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling4(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40));
      // Verify
      assertThat(actual).isEqualTo(10);
    }

    @Test
    void throwing_2() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .exceptionFrom(ExpectedExtendedRuntimeException1::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling4(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40));
      // Verify
      assertThat(actual).isEqualTo(21);
    }

    @Test
    void throwing_3() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .exceptionFrom(ExpectedExtendedRuntimeException2::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling4(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40));
      // Verify
      assertThat(actual).isEqualTo(32);
    }


    @Test
    void throwing_4() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .exceptionFrom(ExpectedExtendedRuntimeException3::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling4(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40));
      // Verify
      assertThat(actual).isEqualTo(43);
    }

    @Test
    void throwing_unexpected() {
      // SetUp
      Deferred<
          Integer,
          Exceptions4<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class)
              .exceptionFrom(NullPointerException::new);
      // Exercise
      try {
        Integer actual =
            target.resolve(
                handling4(
                    e -> e.value() + 10,
                    e -> e.value() + 20,
                    e -> e.value() + 30,
                    e -> e.value() + 40));
        // Verify
        fail();
      } catch (UnexpectedExceptionThrownException e) {
        assertThat(e).hasCauseInstanceOf(NullPointerException.class);
      }
    }
  }

  @Nested
  class Exceptions_5 {
    @Test
    void returning_value() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .valueFrom(() -> 1);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(1);
    }

    @Test
    void throwing_1() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(ExpectedExtendedRuntimeException0::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(10);
    }

    @Test
    void throwing_2() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(ExpectedExtendedRuntimeException1::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(21);
    }

    @Test
    void throwing_3() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(ExpectedExtendedRuntimeException2::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(32);
    }


    @Test
    void throwing_4() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(ExpectedExtendedRuntimeException3::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(43);
    }

    @Test
    void throwing_5() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(ExpectedExtendedRuntimeException4::new);
      // Exercise
      Integer actual =
          target.resolve(
              handling5(
                  e -> e.value() + 10,
                  e -> e.value() + 20,
                  e -> e.value() + 30,
                  e -> e.value() + 40,
                  e -> e.value() + 50));
      // Verify
      assertThat(actual).isEqualTo(54);
    }


    @Test
    void throwing_unexpected() {
      // SetUp
      Deferred<
          Integer,
          Exceptions5<
              ExpectedExtendedRuntimeException0,
              ExpectedExtendedRuntimeException1,
              ExpectedExtendedRuntimeException2,
              ExpectedExtendedRuntimeException3,
              ExpectedExtendedRuntimeException4
              >> target =
          Deferred
              .possiblyThrow(
                  ExpectedExtendedRuntimeException0.class,
                  ExpectedExtendedRuntimeException1.class,
                  ExpectedExtendedRuntimeException2.class,
                  ExpectedExtendedRuntimeException3.class,
                  ExpectedExtendedRuntimeException4.class)
              .exceptionFrom(NullPointerException::new);
      // Exercise
      try {
        Integer actual =
            target.resolve(
                handling5(
                    e -> e.value() + 10,
                    e -> e.value() + 20,
                    e -> e.value() + 30,
                    e -> e.value() + 40,
                    e -> e.value() + 50));
        // Verify
        fail();
      } catch (UnexpectedExceptionThrownException e) {
        assertThat(e).hasCauseInstanceOf(NullPointerException.class);
      }
    }
  }

  @Nested
  class KeepingStackTrace {
    @Test
    void returnsValue() throws InterruptedException {
      //Setup
      PausableThreadPoolExecutor executor = new PausableThreadPoolExecutor();
      executor.pause();
      CompletableFuture<Integer> target = this.asyncApplication(1, executor)
          .thenApply(
              resolveWith(
                  handling1(
                      e -> {
                        throw e;
                      })));
      //Exercise
      Thread.sleep(1000);
      executor.resume();
      long start = System.nanoTime();
      Integer actual = target.join();
      long end = System.nanoTime();
      assertThat(actual).isEqualTo(2);
      assertThat((end - start) / 1000 / 1000).isGreaterThanOrEqualTo(1000);
    }

    @Test
    void keepsOriginalStackTrace() throws InterruptedException {
      //Setup
      PausableThreadPoolExecutor executor = new PausableThreadPoolExecutor();
      executor.pause();
      CompletableFuture<Integer> target = this.asyncApplication(2, executor)
          .thenApply(
              resolveWith(
                  handling1(
                      e -> {
                        //Verify
                        assertThat(e)
                            .hasMessageContaining("DeferredTest")
                            .hasStackTraceContaining("service")
                            .hasStackTraceContaining("add1To")
                            .hasStackTraceContaining("validateToBe1");
                        return 0;
                      })));
      //Exercise
      Thread.sleep(1000);
      executor.resume();
      long start = System.nanoTime();
      target.join();
      long end = System.nanoTime();
      assertThat((end - start) / 1000 / 1000).isGreaterThanOrEqualTo(1000);
    }

    CompletableFuture<Deferred<Integer, Exception1<IllegalArgumentException>>> asyncApplication(int input, Executor executor) {
      return supplyAsync(() -> this.service(input), executor);
    }

    Deferred<Integer, Exception1<IllegalArgumentException>> service(int input) {

      try {
        Thread.sleep(1000);
        return this.deferred().value(this.add1To(input));
      } catch (IllegalArgumentException e) {
        return this.deferred().exception(e);
      } catch (InterruptedException e) {
        return this.deferred().exceptionFrom(() -> new RuntimeException(e));
      }
    }

    ExceptionsPartialApplication<Exception1<IllegalArgumentException>> deferred() {
      return Deferred.possiblyThrow(IllegalArgumentException.class);
    }

    int add1To(int input) {
      this.validateToBe1(input);
      return input + 1;
    }

    void validateToBe1(int input) {
      if (!Objects.equals(input, 1)) {
        throw new IllegalArgumentException(Thread.currentThread().getName());
      }
    }

    class PausableThreadPoolExecutor extends ThreadPoolExecutor {
      private boolean isPaused;
      private ReentrantLock pauseLock = new ReentrantLock();
      private Condition unPaused = pauseLock.newCondition();

      public PausableThreadPoolExecutor() {
        super(5, 10, 10000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20));
        setThreadFactory(r -> new Thread(r, "DeferredTest"));
      }

      protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
          while (isPaused) {
            unPaused.await();
          }
        } catch (InterruptedException ie) {
          t.interrupt();
        } finally {
          pauseLock.unlock();
        }
      }

      public void pause() {
        pauseLock.lock();
        try {
          isPaused = true;
        } finally {
          pauseLock.unlock();
        }
      }

      public void resume() {
        pauseLock.lock();
        try {
          isPaused = false;
          unPaused.signalAll();
        } finally {
          pauseLock.unlock();
        }
      }
    }
  }

  private static abstract class ExpectedRuntimeException extends RuntimeException {
    abstract int value();

    ExpectedRuntimeException(String message) {
      super(message);
    }
  }

  private static class ExpectedExtendedRuntimeException0 extends ExpectedRuntimeException {
    @Override
    int value() {
      return 0;
    }

    ExpectedExtendedRuntimeException0() {
      super("this is expected");
    }
  }

  private static class ExpectedExtendedRuntimeException1 extends ExpectedRuntimeException {
    @Override
    int value() {
      return 1;
    }

    ExpectedExtendedRuntimeException1() {
      super("this is expected");
    }
  }

  private static class ExpectedExtendedRuntimeException2 extends ExpectedRuntimeException {
    @Override
    int value() {
      return 2;
    }

    ExpectedExtendedRuntimeException2() {
      super("this is expected");
    }
  }

  private static class ExpectedExtendedRuntimeException3 extends ExpectedRuntimeException {
    @Override
    int value() {
      return 3;
    }

    ExpectedExtendedRuntimeException3() {
      super("this is expected");
    }
  }


  private static class ExpectedExtendedRuntimeException4 extends ExpectedRuntimeException {
    @Override
    int value() {
      return 4;
    }

    ExpectedExtendedRuntimeException4() {
      super("this is expected");
    }
  }

  private static class ExpectedNonRuntimeException extends Exception {
    ExpectedNonRuntimeException() {
      super("this is expected B");
    }
  }
}