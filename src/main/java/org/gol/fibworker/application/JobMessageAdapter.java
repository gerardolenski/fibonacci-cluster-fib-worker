package org.gol.fibworker.application;

import com.google.gson.Gson;

import org.gol.fibworker.domain.fib.FibonacciResult;
import org.gol.fibworker.domain.fib.FibonacciStrategy;
import org.gol.fibworker.domain.fib.FibonacciStrategyFactory;
import org.gol.fibworker.domain.model.FailureMessage;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.TaskId;
import org.gol.fibworker.domain.result.FailureResultCmd;
import org.gol.fibworker.domain.result.ResultPort;
import org.gol.fibworker.domain.result.SuccessResultCmd;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import io.vavr.control.Try;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker.queue-name}";
    private static final String WORKER_CONCURRENCY_PROPERTY = "${mq.worker.concurrency}";
    private static final Gson GSON = new Gson();

    private final ResultPort resultPort;
    private final FibonacciStrategyFactory fibStrategyFactory;

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
                .peek(payload -> log.debug("Consumed message: {}", payload))
                .map(payload -> GSON.fromJson(payload, FibWorkerMessage.class))
                .map(Optional::of)
                .onFailure(e -> log.error("The message could not be parsed", e))
                .getOrElse(Optional::empty);
    }

    private void processMessage(FibWorkerMessage fibMessage) {
        var fibJob = fibMessage.data();
        log.info("Processing message: {}", fibMessage);

        Try.of(() -> fibStrategyFactory.findStrategy(fibJob.algorithm(), fibJob.number()))
                .mapTry(FibonacciStrategy::calculateFibonacciNumber)
                .onSuccess(result -> sendSuccess(fibMessage.taskId(), fibJob.jobId(), result))
                .onFailure(e -> sendFailure(fibMessage.taskId(), fibJob.jobId(), e));
    }

    private void sendSuccess(UUID taskId, UUID jobId, FibonacciResult result) {
        log.info("Message was processed successfully: taskId={}, jobId={}, result={}", taskId, jobId, result);
        resultPort.sendResult(SuccessResultCmd.builder()
                .taskId(new TaskId(taskId))
                .jobId(new JobId(jobId))
                .result(result)
                .build());
    }

    private void sendFailure(UUID taskId, UUID jobId, Throwable e) {
        log.error("Message was processed with failure: taskId={}, jobId={}", taskId, jobId, e);
        resultPort.sendResult(FailureResultCmd.builder()
                .taskId(new TaskId(taskId))
                .jobId(new JobId(jobId))
                .errorMessage(new FailureMessage(e.getMessage()))
                .build());
    }
}
