package org.gol.fibworker.application;

import com.google.gson.Gson;

import org.gol.fibworker.domain.FibonacciProcessingCmd;
import org.gol.fibworker.domain.FibonacciProcessingPort;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.SequenceBase;
import org.gol.fibworker.domain.model.TaskId;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import io.vavr.control.Try;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Primary adapter working with Artemis Broker.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class JobMessageAdapter {

    private static final String FIBONACCI_SELECTOR = "worker = 'FIBONACCI'";
    private static final String WORKER_QUEUE_NAME_PROPERTY = "${mq.worker.queue-name}";
    private static final String WORKER_CONCURRENCY_PROPERTY = "${mq.worker.concurrency}";
    private static final Gson GSON = new Gson();

    private final FibonacciProcessingPort processingPort;

    @JmsListener(
            destination = WORKER_QUEUE_NAME_PROPERTY,
            selector = FIBONACCI_SELECTOR,
            concurrency = WORKER_CONCURRENCY_PROPERTY)
    void handleFibonacciJob(TextMessage message) {
        parseMessage(message)
                .ifPresent(processingPort::calcFibonacci);
    }

    private Optional<FibonacciProcessingCmd> parseMessage(TextMessage message) {
        return Try.of(message::getText)
                .peek(payload -> log.debug("Consumed message: {}", payload))
                .map(payload -> GSON.fromJson(payload, FibWorkerMessage.class))
                .map(this::toCmd)
                .map(Optional::of)
                .onFailure(e -> log.error("The message could not be parsed", e))
                .getOrElse(Optional::empty);
    }

    private FibonacciProcessingCmd toCmd(FibWorkerMessage message) {
        var fibJob = message.data();
        return FibonacciProcessingCmd.builder()
                .taskId(new TaskId(message.taskId()))
                .jobId(new JobId(fibJob.jobId()))
                .algorithmClaim(new AlgorithmClaim(fibJob.algorithm()))
                .sequenceBase(new SequenceBase(fibJob.number()))
                .build();
    }
}
