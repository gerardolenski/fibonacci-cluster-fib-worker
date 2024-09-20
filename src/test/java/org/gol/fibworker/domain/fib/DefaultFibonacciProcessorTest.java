package org.gol.fibworker.domain.fib;

import org.gol.fibworker.domain.fib.strategy.FibonacciResult;
import org.gol.fibworker.domain.fib.strategy.FibonacciStrategy;
import org.gol.fibworker.domain.fib.strategy.FibonacciStrategyFactory;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.SequenceBase;
import org.gol.fibworker.domain.model.TaskId;
import org.gol.fibworker.domain.result.FailureResultCmd;
import org.gol.fibworker.domain.result.ResultPort;
import org.gol.fibworker.domain.result.SuccessResultCmd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.UUID;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultFibonacciProcessorTest {

    private static final TaskId TASK_ID = new TaskId(UUID.randomUUID());
    private static final JobId JOB_ID = new JobId(UUID.randomUUID());
    private static final AlgorithmClaim ALGORITHM_CLAIM = new AlgorithmClaim("ITERATIVE");
    private static final SequenceBase SEQUENCE_BASE = new SequenceBase(45);
    private static final FibonacciResult FIB_RESULT = new FibonacciResult(BigInteger.valueOf(1134903170), ofMillis(100));
    private static final String ERROR_MESSAGE = "calc error";
    private static final FibonacciStrategy STRATEGY = () -> FIB_RESULT;
    private static final FibonacciProcessingCmd CMD = FibonacciProcessingCmd.builder()
            .taskId(TASK_ID)
            .jobId(JOB_ID)
            .sequenceBase(SEQUENCE_BASE)
            .algorithmClaim(ALGORITHM_CLAIM)
            .build();

    @Mock
    private ResultPort resultPort;
    @Mock
    private FibonacciStrategyFactory strategyFactory;
    @Captor
    private ArgumentCaptor<SuccessResultCmd> successCmdCaptor;
    @Captor
    private ArgumentCaptor<FailureResultCmd> failureCmdCapture;

    private DefaultFibonacciProcessor sut;

    @BeforeEach
    void init() {
        sut = new DefaultFibonacciProcessor(resultPort, strategyFactory);
    }

    @Test
    @DisplayName("should correctly orchestrate Fibonacci number calculation [positive]")
    void shouldProcessCorrectCmd() {
        //given
        doReturn(STRATEGY).when(strategyFactory).findStrategy(ALGORITHM_CLAIM, SEQUENCE_BASE);

        //when
        sut.calcFibonacci(CMD);

        //then
        verify(resultPort, never()).sendResult(isA(FailureResultCmd.class));
        verify(resultPort).sendResult(successCmdCaptor.capture());
        var resultCmd = successCmdCaptor.getValue();
        assertThat(resultCmd.taskId())
                .isEqualTo(TASK_ID);
        assertThat(resultCmd.jobId())
                .isEqualTo(JOB_ID);
        assertThat(resultCmd.result())
                .isEqualTo(FIB_RESULT);
    }

    @Test
    @DisplayName("should handle process failure [negative]")
    void shouldHandleProcessingError() {
        //given
        doThrow(new IllegalStateException(ERROR_MESSAGE)).when(strategyFactory).findStrategy(ALGORITHM_CLAIM, SEQUENCE_BASE);

        //when
        sut.calcFibonacci(CMD);

        //then
        verify(resultPort, never()).sendResult(isA(SuccessResultCmd.class));
        verify(resultPort).sendResult(failureCmdCapture.capture());
        var resultCmd = failureCmdCapture.getValue();
        assertThat(resultCmd.taskId())
                .isEqualTo(TASK_ID);
        assertThat(resultCmd.jobId())
                .isEqualTo(JOB_ID);
        assertThat(resultCmd.errorMessage().value())
                .isEqualTo(ERROR_MESSAGE);
    }

}