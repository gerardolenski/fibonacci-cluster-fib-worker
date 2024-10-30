package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.fib.strategy.FibonacciStrategyFactory;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.SequenceBase;

import lombok.RequiredArgsConstructor;

/**
 * Domain Service handling Fibonacci number calculation. It eagerly calculates number using strategy for the algorithm
 * claim.
 */
@RequiredArgsConstructor
class EagerFibCalculator implements FibCalculator {

    private final FibonacciStrategyFactory fibStrategyFactory;

    @Override
    public FibonacciResult calculateFibonacciNumber(AlgorithmClaim algorithmClaim, SequenceBase sequenceBase) {
        return fibStrategyFactory.findStrategy(algorithmClaim, sequenceBase)
                .calculateFibonacciNumber();
    }
}
