package org.gol.fibworker.application;

import org.apache.commons.lang3.time.StopWatch;
import org.gol.fibworker.domain.fib.FibonacciStrategy;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.gol.fibworker.application.AmqConsumerConfig.WORKER_QUEUE_FACTORY;
import static org.gol.fibworker.domain.fib.FibonacciStrategyFactory.getFibonacciStrategy;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker.queue-name}";
    private static final String WORKER_CONCURRENCY_PROPERTY = "${mq.worker.concurrency}";

    private final ResultPort resultPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = FIBONACCI_SELECTOR,
            concurrency = WORKER_CONCURRENCY_PROPERTY,
            containerFactory = WORKER_QUEUE_FACTORY)
    void handleFibonacciJob(Message<FibWorkerMessage> message) {
        var fibMessage = message.getPayload();
        var fibJob = fibMessage.data();
        log.debug("Consumed message: {}", message.getPayload());

        var watch = StopWatch.create();
        Try.of(() -> getFibonacciStrategy(fibJob.algorithm(), fibJob.number()))
                .andThen(watch::start)
                .map(FibonacciStrategy::calculateFibonacciNumber)
                .andThen(watch::stop)
                .onFailure(e -> log.error("The FIB number could not be calculated: {}", e.getMessage()))
                .onFailure(e -> sendFailure(fibMessage.taskId(), fibJob.jobId(), e))
                .onSuccess(result -> sendSuccess(fibMessage.taskId(), fibJob.jobId(), result, watch.getTime(MILLISECONDS)));
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
