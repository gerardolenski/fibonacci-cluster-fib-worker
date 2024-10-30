package org.gol.fibworker.domain.fib.strategy;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.Duration;

import lombok.NonNull;

public record FibonacciResult(@NonNull BigInteger number, @NonNull Duration executionTime) implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;
}
