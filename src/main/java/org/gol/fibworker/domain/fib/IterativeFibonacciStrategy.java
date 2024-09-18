package org.gol.fibworker.domain.fib;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

@Slf4j
class IterativeFibonacciStrategy extends BaseFibonacciStrategy {

    public IterativeFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger findFib(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;

        var n0 = ZERO;
        var n1 = ONE;
        BigInteger tempNthTerm;
        for (long i = 2; i <= n; i++) {
            tempNthTerm = n0.add(n1);
            n0 = n1;
            n1 = tempNthTerm;
        }
        return n1;
    }
}
