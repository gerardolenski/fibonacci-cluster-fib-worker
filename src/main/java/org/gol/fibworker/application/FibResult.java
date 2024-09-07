package org.gol.fibworker.application;

import org.gol.fibworker.domain.result.ResultData;

import java.math.BigInteger;
import java.util.UUID;

import lombok.Builder;

@Builder
record FibResult(
        UUID jobId,
        BigInteger result,
        Long processingTime,
        String errorMessage) implements ResultData {
}
