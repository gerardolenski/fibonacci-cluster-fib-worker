package org.gol.fibworker.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.gol.fibworker.domain.job.JobData;
import org.gol.fibworker.domain.job.WorkerData;

import java.util.UUID;

record FibWorkerMessage(UUID taskId, FibJobData data) implements WorkerData {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record FibJobData(UUID jobId, Integer number, String algorithm) implements JobData {
    }
}
