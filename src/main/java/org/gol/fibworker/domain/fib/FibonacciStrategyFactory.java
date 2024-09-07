package org.gol.fibworker.domain.fib;

import java.util.Arrays;
import java.util.function.Function;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FibonacciStrategyFactory {

    @RequiredArgsConstructor
    private enum Algorithm implements Function<Integer, FibonacciStrategy> {
        RECURSIVE(RecursiveFibonacciStrategy::new),
        ITERATIVE(IterativeFibonacciStrategy::new),
        BINETS(BinetsFibonacciStrategy::new),
        EXPONENTIAL(ExponentialFibonacciStrategy::new);

        private final Function<Integer, FibonacciStrategy> mapper;

        @Override
        public FibonacciStrategy apply(Integer num) {
            return this.mapper.apply(num);
        }
    }

    public static FibonacciStrategy getFibonacciStrategy(String algorithm, Integer number) {
        return Arrays.stream(Algorithm.values())
                .filter(a -> a.name().equals(algorithm))
                .findFirst()
                .map(a -> a.apply(number))
                .orElseThrow();
    }
}
