package org.gol.fibworker.domain.fib.strategy;

import java.math.BigInteger;

import lombok.extern.slf4j.Slf4j;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.util.stream.Stream.iterate;

@Slf4j
class StreamingIterativeFibonacciStrategy extends BaseFibonacciStrategy {

    public StreamingIterativeFibonacciStrategy(int num) {
        super(num, log);
    }

    @Override
    BigInteger findFib(int n) {
        return iterate(new FibEl(ZERO, ONE), prev -> new FibEl(prev.n2, prev.sum()))
                .limit(n)
                .map(FibEl::n2)
                .reduce(ZERO, (n1, n2) -> n2);
    }

    record FibEl(BigInteger n1, BigInteger n2) {
        BigInteger sum() {
            return n1.add(n2);
        }
    }
}
