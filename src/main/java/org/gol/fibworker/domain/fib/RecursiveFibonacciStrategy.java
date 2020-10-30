package org.gol.fibworker.domain.fib;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

import static java.lang.String.format;

@Slf4j
class RecursiveFibonacciStrategy extends BaseFibonacciAlgorithm {

    public RecursiveFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger getFib(int n) {
        if (n > 45) {
            throw new UnsupportedOperationException(format("Number %d is too big for RECURSIVE FIBONACCI ALGORITHM", n));
        }
        return BigInteger.valueOf(f(n));
    }

    private long f(int n) {
        if (n <= 1)
            return n;
        return f(n - 1) + f(n - 2);
    }
}
