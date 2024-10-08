package org.gol.fibworker.domain.fib.strategy;

import java.math.BigInteger;
import java.time.Duration;

import lombok.NonNull;

public record FibonacciResult(@NonNull BigInteger number, @NonNull Duration executionTime) {
}
