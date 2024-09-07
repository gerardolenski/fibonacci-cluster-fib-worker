package org.gol.fibworker.infrastructure.amq;

import java.math.BigInteger;
import java.util.UUID;

import lombok.Builder;

@Builder
record FibResultMessage(
        UUID taskId,
        UUID jobId,
        BigInteger result,
        Long processingTime,
        String errorMessage) {
}
