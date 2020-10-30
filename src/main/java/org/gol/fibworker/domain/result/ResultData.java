package org.gol.fibworker.domain.result;

import java.math.BigInteger;
import java.util.UUID;

public interface ResultData {
    UUID getJobId();

    BigInteger getResult();

    Long getProcessingTime();

    String getErrorMessage();
}
