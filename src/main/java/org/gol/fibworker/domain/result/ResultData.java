package org.gol.fibworker.domain.result;

import java.math.BigInteger;
import java.util.UUID;

public interface ResultData {
    UUID jobId();

    BigInteger result();

    Long processingTime();

    String errorMessage();
}
