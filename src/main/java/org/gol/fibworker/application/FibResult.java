package org.gol.fibworker.application;

import lombok.Builder;
import lombok.Getter;
import org.gol.fibworker.domain.result.ResultData;

import java.math.BigInteger;
import java.util.UUID;

@Getter
@Builder
class FibResult implements ResultData {
    private final UUID jobId;
    private final BigInteger result;
}
