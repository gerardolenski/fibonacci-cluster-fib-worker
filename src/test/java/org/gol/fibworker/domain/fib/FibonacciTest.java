package org.gol.fibworker.domain.fib;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

class FibonacciTest {

    @ParameterizedTest(name = "{index}. fib of {0}")
    @MethodSource("fibonacciSupplier")
    @DisplayName("should calculate fibonacci by iterative strategy [positive]")
    void iterativeFibonacci(int num, BigInteger expectResult) {
        //given
        var sut = new IterativeFibonacciStrategy(num);

        //when
        var result = sut.calculateFibonacciNumber();

        //then
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest(name = "{index}. fib of {0}")
    @MethodSource("fibonacciSupplier")
    @DisplayName("should calculate fibonacci by recursive strategy [positive]")
    void recursiveFibonacci(int num, BigInteger expectResult) {
        //given
        var sut = new RecursiveFibonacciStrategy(num);

        //when
        var result = sut.calculateFibonacciNumber();

        //then
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest(name = "{index}. fib of {0}")
    @MethodSource("fibonacciSupplier")
    @DisplayName("should calculate fibonacci by binets strategy [positive]")
    void binetsFibonacci(int num, BigInteger expectResult) {
        //given
        var sut = new BinetsFibonacciStrategy(num);

        //when
        var result = sut.calculateFibonacciNumber();

        //then
        assertThat(result).isEqualTo(expectResult);
    }

    @ParameterizedTest(name = "{index}. fib of {0}")
    @MethodSource("fibonacciSupplier")
    @DisplayName("should calculate fibonacci by exponential strategy [positive]")
    void exponentialFibonacci(int num, BigInteger expectResult) {
        //given
        var sut = new ExponentialFibonacciStrategy(num);

        //when
        var result = sut.calculateFibonacciNumber();

        //then
        assertThat(result).isEqualTo(expectResult);
    }

    private static Stream<Arguments> fibonacciSupplier() {
        return Stream.of(
                Arguments.of(0, ZERO),
                Arguments.of(1, ONE),
                Arguments.of(10, BigInteger.valueOf(55)),
                Arguments.of(20, BigInteger.valueOf(6765)),
                Arguments.of(30, BigInteger.valueOf(832040)),
                Arguments.of(40, BigInteger.valueOf(102334155))
        );
    }
}