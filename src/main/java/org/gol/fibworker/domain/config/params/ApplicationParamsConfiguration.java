package org.gol.fibworker.domain.config.params;

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

    private final String workerQueueName;
    private final String workerConcurrency;
    private final String workerMessageTypeId;

    ApplicationParamsConfiguration(
            @Value(WORKER_QUEUE_NAME_PROPERTY) String workerQueueName,
            @Value(WORKER_CONCURRENCY_PROPERTY) String workerConcurrency,
            @Value(WORKER_MESSAGE_TYPE_ID_PROPERTY) String workerMessageTypeId) {
        this.workerQueueName = workerQueueName;
        this.workerConcurrency = workerConcurrency;
        this.workerMessageTypeId = workerMessageTypeId;
        log.info("Init APPLICATION PARAMS configuration component: {}", this);
    }
}
