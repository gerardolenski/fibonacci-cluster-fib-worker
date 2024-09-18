package org.gol.fibworker.domain.result;

import org.gol.fibworker.domain.model.FailureMessage;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.TaskId;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FailureResultCmd(
        @NonNull TaskId taskId,
        @NonNull JobId jobId,
        @NonNull FailureMessage errorMessage) {
}
