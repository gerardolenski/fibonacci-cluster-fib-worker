package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.fib.strategy.FibonacciStrategy;
import org.gol.fibworker.domain.fib.strategy.FibonacciStrategyFactory;
import org.gol.fibworker.domain.model.FailureMessage;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.TaskId;
import org.gol.fibworker.domain.result.FailureResultCmd;
import org.gol.fibworker.domain.result.ResultPort;
import org.gol.fibworker.domain.result.SuccessResultCmd;
import org.springframework.stereotype.Service;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Application Service orchestrating Fibonacci number processing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
class DefaultFibonacciProcessor implements FibonacciProcessingPort {

    private final ResultPort resultPort;
    private final FibonacciStrategyFactory fibStrategyFactory;

    @Override
    public void calcFibonacci(FibonacciProcessingCmd cmd) {
        log.info("Starting Fibonacci calculation: taskId={}, jobId={}, algorithmClaim={}, sequenceBase={}",
                cmd.taskId().value(), cmd.jobId().value(), cmd.algorithmClaim().value(), cmd.sequenceBase().value());
        Try.of(() -> fibStrategyFactory.findStrategy(cmd.algorithmClaim(), cmd.sequenceBase()))
                .mapTry(FibonacciStrategy::calculateFibonacciNumber)
                .onSuccess(result -> sendSuccess(cmd.taskId(), cmd.jobId(), result))
                .onFailure(e -> sendFailure(cmd.taskId(), cmd.jobId(), e));
    }

    private void sendSuccess(TaskId taskId, JobId jobId, FibonacciResult result) {
        log.info("Fibonacci calculation was processed successfully: taskId={}, jobId={}, result={}", taskId.value(), jobId.value(), result);
        resultPort.sendResult(SuccessResultCmd.builder()
                .taskId(taskId)
                .jobId(jobId)
                .result(result)
                .build());
    }

    private void sendFailure(TaskId taskId, JobId jobId, Throwable e) {
        log.error("Fibonacci calculation was processed with failure: taskId={}, jobId={}", taskId.value(), jobId.value(), e);
        resultPort.sendResult(FailureResultCmd.builder()
                .taskId(taskId)
                .jobId(jobId)
                .errorMessage(new FailureMessage(e.getMessage()))
                .build());
    }
}
