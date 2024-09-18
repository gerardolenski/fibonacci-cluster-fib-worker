package org.gol.fibworker.domain.fib;

@FunctionalInterface
public interface FibonacciStrategy {
    FibonacciResult calculateFibonacciNumber();
}
