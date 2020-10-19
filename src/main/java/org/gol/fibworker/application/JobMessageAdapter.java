package org.gol.fibworker.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.fibworker.domain.config.params.ConfigurationPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.gol.fibworker.application.AmqConfig.WORKER_QUEUE_FACTORY;
import static org.gol.fibworker.domain.config.params.ConfigurationPort.WORKER_CONCURRENCY_PROPERTY;
import static org.gol.fibworker.domain.config.params.ConfigurationPort.WORKER_QUEUE_NAME_PROPERTY;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";

    private final ConfigurationPort configurationPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = FIBONACCI_SELECTOR,
            concurrency = WORKER_CONCURRENCY_PROPERTY,
            containerFactory = WORKER_QUEUE_FACTORY)
    void handleFibonacciJob(Message<FibWorkerMessage> message) {
        log.debug("Consume message: {}", message.getPayload());
    }

    @PostConstruct
    void init() {
        log.info("Initialized JOB MESSAGE LISTENER with queue: {} and concurrency: {}",
                configurationPort.getWorkerQueueName(), configurationPort.getWorkerConcurrency());
    }
}
