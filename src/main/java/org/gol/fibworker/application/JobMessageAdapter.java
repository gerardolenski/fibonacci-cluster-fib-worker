package org.gol.fibworker.application;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.fibworker.domain.config.params.ConfigurationPort;
import org.gol.fibworker.domain.fib.FibonacciPort;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static org.gol.fibworker.application.AmqConfig.WORKER_QUEUE_FACTORY;
import static org.gol.fibworker.domain.config.params.ConfigurationPort.WORKER_CONCURRENCY_PROPERTY;
import static org.gol.fibworker.domain.config.params.ConfigurationPort.WORKER_QUEUE_NAME_PROPERTY;
import static org.gol.fibworker.domain.fib.FibonacciStrategyFactory.getFibonacciStrategy;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";

    private final ConfigurationPort configurationPort;
    private final ResultPort resultPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = FIBONACCI_SELECTOR,
            concurrency = WORKER_CONCURRENCY_PROPERTY,
            containerFactory = WORKER_QUEUE_FACTORY)
    void handleFibonacciJob(Message<FibWorkerMessage> message) {
        var fibMessage = message.getPayload();
        var fibJob = fibMessage.getJobData();
        log.debug("Consume message: {}", message.getPayload());

        var fibOptional = Try.ofSupplier(() -> getFibonacciStrategy(fibJob.getAlgorithm(), fibJob.getNumber()))
                .map(FibonacciPort::calculateFibonacciNumber)
                .map(Optional::of)
                .onFailure(e -> log.error("The FIB number could not be calculated: {}", e.getMessage()))
                .getOrElse(Optional.empty());

        resultPort.sendResult(
                fibMessage.getTaskId(),
                FibResult.builder()
                        .jobId(fibJob.getJobId())
                        .result(fibOptional.orElse(null))
                        .build());
    }

    @PostConstruct
    void init() {
        log.info("Initialized JOB MESSAGE LISTENER with queue: {} and concurrency: {}",
                configurationPort.getWorkerQueueName(), configurationPort.getWorkerConcurrency());
    }
}
