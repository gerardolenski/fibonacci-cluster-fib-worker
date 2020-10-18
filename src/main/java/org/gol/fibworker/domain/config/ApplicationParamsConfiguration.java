package org.gol.fibworker.domain.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@ToString
@Component
class ApplicationParamsConfiguration implements ConfigurationPort {
    public static final String WORKER_QUEUE_NAME = "${mq.worker-queue-name}";
    public static final String WORKER_CONCURRENCY = "${mq.worker-concurrency}";

    private final String workerQueueName;
    private final String workerConcurrency;


    ApplicationParamsConfiguration(
            @Value(WORKER_QUEUE_NAME) String workerQueueName,
            @Value(WORKER_CONCURRENCY) String workerConcurrency) {
        this.workerQueueName = workerQueueName;
        this.workerConcurrency = workerConcurrency;
        log.info("Init APPLICATION PARAMS configuration component: {}", this);
    }
}
