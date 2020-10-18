package org.gol.fibworker.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.fibworker.domain.config.ConfigurationPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";

    private final ConfigurationPort configurationPort;

    @JmsListener(
            destination = "${mq.worker-queue-name}",
            selector = FIBONACCI_SELECTOR,
            concurrency = "${mq.worker-concurrency}")
    void handleFibonacciJob(Message<Object> message) {
        log.debug("Consume message: {}", message.getPayload());
    }

    @PostConstruct
    void init() {
        log.info("Initialized JOB MESSAGE LISTENER with queue: {} and concurrency: {}",
                configurationPort.getWorkerQueueName(), configurationPort.getWorkerConcurrency());
    }
}
