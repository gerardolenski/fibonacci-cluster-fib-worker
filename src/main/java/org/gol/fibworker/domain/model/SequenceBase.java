package org.gol.fibworker.domain.model;

/**
 * Value Object representing Fibonacci sequence base number.
 */
public record SequenceBase(int value) {

    public SequenceBase {
        if (value < 0)
            throw new IllegalArgumentException("Fibonacci sequence sequenceBase must be natural number");
    }
}
