package org.gol.fibworker.domain.fib.strategy;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
class RecursiveFibonacciStrategy extends BaseFibonacciStrategy {

    public RecursiveFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger findFib(int n) {
        if (n > 49)
            throw new UnsupportedOperationException(format("Number %d is too big for RECURSIVE FIBONACCI ALGORITHM", n));
        return BigInteger.valueOf(f(n));
    }

    private long f(int n) {
        if (n <= 1) return n;
        return f(n - 1) + f(n - 2);
    }
}
