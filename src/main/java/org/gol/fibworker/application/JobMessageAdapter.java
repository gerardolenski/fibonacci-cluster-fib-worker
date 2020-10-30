package org.gol.fibworker.application;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.gol.fibworker.domain.config.params.ConfigurationPort;
import org.gol.fibworker.domain.fib.FibonacciPort;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
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

        var watch = StopWatch.create();
        Try.of(() -> getFibonacciStrategy(fibJob.getAlgorithm(), fibJob.getNumber()))
                .andThen(watch::start)
                .map(FibonacciPort::calculateFibonacciNumber)
                .andThen(watch::stop)
                .onFailure(e -> log.error("The FIB number could not be calculated: {}", e.getMessage()))
                .onFailure(e -> sendFailure(fibMessage.getTaskId(), fibJob.getJobId(), e))
                .onSuccess(result -> sendSuccess(fibMessage.getTaskId(), fibJob.getJobId(), result, watch.getTime(MILLISECONDS)));
    }

    @PostConstruct
    void init() {
        log.info("Initialized JOB MESSAGE LISTENER with queue: {} and concurrency: {}",
                configurationPort.getWorkerQueueName(), configurationPort.getWorkerConcurrency());
    }

    private void sendSuccess(UUID taskId, UUID jobId, BigInteger result, long processingTime) {
        resultPort.sendResult(
                taskId,
                FibResult.builder()
                        .jobId(jobId)
                        .result(result)
                        .processingTime(processingTime)
                        .build());
    }

    private void sendFailure(UUID taskId, UUID jobId, Throwable e) {
        resultPort.sendResult(
                taskId,
                FibResult.builder()
                        .jobId(jobId)
                        .errorMessage(e.getMessage())
                        .build());
    }
}
