package org.gol.fibworker.domain.fib;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FibonacciTest {

    @ParameterizedTest(name = "{index}. fib of {0}")
    @MethodSource("fibonacciSupplier")
    void fib0(int num, BigInteger expectResult) {
        assertEquals(expectResult, new IterativeFibonacciStrategy(num).calculateFibonacciNumber());
        assertEquals(expectResult, new RecursiveFibonacciStrategy(num).calculateFibonacciNumber());
        assertEquals(expectResult, new BinetsFibonacciStrategy(num).calculateFibonacciNumber());
        assertEquals(expectResult, new ExponentialFibonacciStrategy(num).calculateFibonacciNumber());
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