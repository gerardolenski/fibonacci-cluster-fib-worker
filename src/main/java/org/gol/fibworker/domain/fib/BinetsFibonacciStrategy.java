package org.gol.fibworker.domain.fib;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
class BinetsFibonacciStrategy extends BaseFibonacciStrategy {

    private static final double SQRT_5 = Math.sqrt(5);
    private static final double PHI = (1 + SQRT_5) / 2;
    private static final double N_PHI = -PHI;

    BinetsFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger getFib(int n) {
        if (n > 71) {
            throw new UnsupportedOperationException(format("Number %d is too big for BINETS FIBONACCI ALGORITHM to be accurate.", n));
        }
        return BigInteger.valueOf((long) ((Math.pow(PHI, n) - Math.pow(N_PHI, -n)) / SQRT_5));
    }
}
