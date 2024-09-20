package org.gol.fibworker.domain.result;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.TaskId;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SuccessResultCmd(
        @NonNull TaskId taskId,
        @NonNull JobId jobId,
        @NonNull FibonacciResult result) {
}
