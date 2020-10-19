package org.gol.fibworker.domain.config.params;

public interface ConfigurationPort {
    String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker.queue-name}";
    String WORKER_CONCURRENCY_PROPERTY = "${mq.worker.concurrency}";
    String WORKER_MESSAGE_TYPE_ID_PROPERTY = "${mq.worker.message-type-id}";

    String getWorkerQueueName();
    String getWorkerConcurrency();
    String getWorkerMessageTypeId();
}
