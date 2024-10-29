package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.SequenceBase;

/**
 * Contract of Fibonacci number calculation for Domain Services.
 */
public interface FibCalculator {

    /**
     * @param algorithmClaim the claim of algorithm to use
     * @param sequenceBase   the sequence base number
     * @return the fibonacci result
     */
    FibonacciResult calculateFibonacciNumber(AlgorithmClaim algorithmClaim, SequenceBase sequenceBase);
}
