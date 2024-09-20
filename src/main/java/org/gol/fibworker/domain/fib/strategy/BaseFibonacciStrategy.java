package org.gol.fibworker.domain.fib.strategy;

import org.slf4j.Logger;

import java.math.BigInteger;

import lombok.RequiredArgsConstructor;

import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.time.StopWatch.createStarted;

@RequiredArgsConstructor
abstract class BaseFibonacciStrategy implements FibonacciStrategy {

    private final int num;
    private final Logger log;

    @Override
    public FibonacciResult calculateFibonacciNumber() {
        log.debug("Calculating Fibonacci number: sequenceBase={}", num);
        var watch = createStarted();
        var fib = findFib(num);
        watch.stop();
        log.debug("Calculated Fibonacci number: fib({}) = {}, executionTime={}", num, fib, watch.getTime(MILLISECONDS));
        return new FibonacciResult(fib, ofMillis(watch.getTime(MILLISECONDS)));
    }

    abstract BigInteger findFib(int n);
}
