package org.gol.fibworker.domain.fib;

/**
 * The primary (driving) port of Fibonacci number processing flow.
 */
public interface FibonacciProcessingPort {

    void calcFibonacci(FibonacciProcessingCmd cmd);
}
