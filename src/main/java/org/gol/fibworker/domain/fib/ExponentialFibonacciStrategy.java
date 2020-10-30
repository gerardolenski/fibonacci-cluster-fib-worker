package org.gol.fibworker.domain.fib;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

@Slf4j
class ExponentialFibonacciStrategy extends BaseFibonacciStrategy {

    private static final BigInteger[][] MATRIX = {{ONE, ONE}, {ONE, ZERO}};

    ExponentialFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger getFib(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return power(MATRIX, n - 1)[0][0];
    }

    private static BigInteger[][] power(BigInteger[][] m, int power) {
        if (power == 1) return m;
        var sub = power(m, power / 2);
        if (power % 2 == 0) {
            return multiply(sub, sub);
        } else {
            return multiply(multiply(sub, sub), m);
        }
    }

    private static BigInteger[][] multiply(BigInteger[][] m1, BigInteger[][] m2) {
        return new BigInteger[][]{
                {m1[0][0].multiply(m2[0][0]).add(m1[0][1].multiply(m2[1][0])),
                        m1[0][0].multiply(m2[0][1]).add(m1[0][1].multiply(m2[1][1]))},
                {m1[1][0].multiply(m2[0][0]).add(m1[1][1].multiply(m2[1][0])),
                        m1[1][0].multiply(m2[0][1]).add(m1[1][1].multiply(m2[1][1]))}
        };
    }
}
