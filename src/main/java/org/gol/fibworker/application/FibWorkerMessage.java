package org.gol.fibworker.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

record FibWorkerMessage(UUID taskId, FibJobData data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record FibJobData(UUID jobId, Integer number, String algorithm) {
    }
}
