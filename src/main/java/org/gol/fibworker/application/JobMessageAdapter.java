package org.gol.fibworker.application;

import com.google.gson.Gson;

import org.apache.commons.lang3.time.StopWatch;
import org.gol.fibworker.domain.fib.FibonacciStrategy;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.vavr.control.Try;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.gol.fibworker.domain.fib.FibonacciStrategyFactory.getFibonacciStrategy;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker.queue-name}";
    private static final String WORKER_CONCURRENCY_PROPERTY = "${mq.worker.concurrency}";
    private static final Gson GSON = new Gson();

    private final ResultPort resultPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = FIBONACCI_SELECTOR,
            concurrency = WORKER_CONCURRENCY_PROPERTY)
    void handleFibonacciJob(TextMessage message) {
        parseMessage(message)
                .ifPresent(this::processMessage);
    }

    private Optional<FibWorkerMessage> parseMessage(TextMessage message) {
        return Try.of(message::getText)
                .map(payload -> GSON.fromJson(payload, FibWorkerMessage.class))
                .map(Optional::of)
                .onFailure(e -> log.error("The message could not be parsed", e))
                .getOrElse(Optional::empty);
    }

    private void processMessage(FibWorkerMessage fibMessage) {
        var fibJob = fibMessage.data();
        log.debug("Consumed message: {}", fibMessage);

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
