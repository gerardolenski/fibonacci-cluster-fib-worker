package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.SequenceBase;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import lombok.RequiredArgsConstructor;

/**
 * Domain Service handling Fibonacci number calculation. It caches calculated number by specific algorithm.
 */
@CacheConfig(cacheNames = CachingFibCalculator.FIB_CALC_CACHE)
@RequiredArgsConstructor
class CachingFibCalculator implements FibCalculator {

    static final String FIB_CALC_CACHE = "fibCalc";

    private final FibCalculator calc;

    @Override
    @Cacheable(key = "{#algorithmClaim.value(), #sequenceBase.value()}")
    public FibonacciResult calculateFibonacciNumber(AlgorithmClaim algorithmClaim, SequenceBase sequenceBase) {
        return calc.calculateFibonacciNumber(algorithmClaim, sequenceBase);
    }
}
