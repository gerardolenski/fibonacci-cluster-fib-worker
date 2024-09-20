package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.SequenceBase;
import org.gol.fibworker.domain.model.TaskId;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FibonacciProcessingCmd(
        @NonNull TaskId taskId,
        @NonNull JobId jobId,
        @NonNull SequenceBase sequenceBase,
        @NonNull AlgorithmClaim algorithmClaim) {
}
