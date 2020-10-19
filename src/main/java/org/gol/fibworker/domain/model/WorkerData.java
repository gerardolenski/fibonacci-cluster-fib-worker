package org.gol.fibworker.domain.model;

import java.util.UUID;

public interface WorkerData {

    UUID getTaskId();

    JobData getJobData();
}
