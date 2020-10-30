package org.gol.fibworker.domain.job;

import java.util.UUID;

public interface JobData {
    UUID getJobId();
    Integer getNumber();
    String getAlgorithm();
}
