package org.gol.fibworker.domain.fib;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.math.BigInteger;

@RequiredArgsConstructor
abstract class BaseFibonacciStrategy implements FibonacciPort {

    private final int num;
    private final Logger log;

    @Override
    public BigInteger calculateFibonacciNumber() {
        log.debug("Calculating Fibonacci number of {}", num);
        var watch = StopWatch.createStarted();
        var fib = getFib(num);
        watch.stop();
        log.trace("fib({}) = {}", num, fib);
        log.debug("Calculated Fibonacci number of {} in {}", num, watch.formatTime());
        return fib;
    }

    abstract BigInteger getFib(int n);
}
