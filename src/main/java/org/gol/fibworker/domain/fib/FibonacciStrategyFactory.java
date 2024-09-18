package org.gol.fibworker.domain.fib;

import org.springframework.stereotype.Component;

import java.util.function.Function;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Component
public class FibonacciStrategyFactory {

    @RequiredArgsConstructor
    private enum Algorithm implements Function<Integer, FibonacciStrategy> {
        RECURSIVE(RecursiveFibonacciStrategy::new),
        ITERATIVE(IterativeFibonacciStrategy::new),
        BINETS(BinetsFibonacciStrategy::new),
        EXPONENTIAL(ExponentialFibonacciStrategy::new),
        STREAMING_ITERATIVE(StreamingIterativeFibonacciStrategy::new);

        private final Function<Integer, FibonacciStrategy> mapper;

        @Override
        public FibonacciStrategy apply(Integer num) {
            return this.mapper.apply(num);
        }
    }

    public FibonacciStrategy findStrategy(String algorithm, Integer number) {
        return stream(Algorithm.values())
                .filter(a -> a.name().equals(algorithm))
                .findFirst()
                .map(a -> a.apply(number))
                .orElseThrow(() -> new IllegalArgumentException(format("Unknown Fibonacci algorithm: algorithm=%s", algorithm)));
    }
}
