package org.gol.fibworker.domain.job;

import java.util.UUID;

public interface WorkerData {
    UUID getTaskId();
    JobData getJobData();
}
