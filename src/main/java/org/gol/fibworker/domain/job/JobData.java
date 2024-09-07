package org.gol.fibworker.domain.job;

import java.util.UUID;

public interface JobData {
    UUID jobId();
    Integer number();
    String algorithm();
}
