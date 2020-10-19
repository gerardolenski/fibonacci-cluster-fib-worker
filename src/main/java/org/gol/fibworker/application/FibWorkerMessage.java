package org.gol.fibworker.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.gol.fibworker.domain.model.JobData;
import org.gol.fibworker.domain.model.WorkerData;

import java.util.UUID;

@ToString
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
class FibWorkerMessage implements WorkerData {
    private final String taskId;
    private final FibJobData data;

    @Override
    public UUID getTaskId() {
        return UUID.fromString(taskId);
    }

    @Override
    public JobData getJobData() {
        return data;
    }

    @Getter
    @ToString
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    static class FibJobData implements JobData {
        private final Integer number;
        private final String algorithm;
    }
}
