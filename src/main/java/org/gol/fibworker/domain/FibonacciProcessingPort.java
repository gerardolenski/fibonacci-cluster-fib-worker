package org.gol.fibworker.domain;

/**
 * The primary (driving) port of Fibonacci number processing flow.
 */
public interface FibonacciProcessingPort {

    void calcFibonacci(FibonacciProcessingCmd cmd);
}
