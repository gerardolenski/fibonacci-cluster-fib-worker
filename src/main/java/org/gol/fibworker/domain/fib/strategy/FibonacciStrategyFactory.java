package org.gol.fibworker.domain.fib.strategy;

import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.SequenceBase;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

    public FibonacciStrategy findStrategy(AlgorithmClaim algorithmClaim, SequenceBase sequenceBase) {
        return stream(Algorithm.values())
                .filter(a -> a.name().equals(algorithmClaim.value()))
                .findFirst()
                .map(a -> a.apply(sequenceBase.value()))
                .orElseGet(() -> new StreamingIterativeFibonacciStrategy(sequenceBase.value()));
    }
}
