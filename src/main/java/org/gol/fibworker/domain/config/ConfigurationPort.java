package org.gol.fibworker.domain.config;

public interface ConfigurationPort {
    String getWorkerQueueName();
    String getWorkerConcurrency();
}
