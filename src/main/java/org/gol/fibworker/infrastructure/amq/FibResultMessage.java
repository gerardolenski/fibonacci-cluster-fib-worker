package org.gol.fibworker.infrastructure.amq;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.UUID;

@Getter
@Builder
@ToString
class FibResultMessage {
    private final UUID taskId;
    private final UUID jobId;
    private final BigInteger result;
    private final Long processingTime;
    private final String errorMessage;
}
