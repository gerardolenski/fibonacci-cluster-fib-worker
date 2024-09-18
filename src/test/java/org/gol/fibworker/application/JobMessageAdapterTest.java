package org.gol.fibworker.application;

import org.gol.fibworker.domain.fib.FibonacciResult;
import org.gol.fibworker.domain.fib.FibonacciStrategy;
import org.gol.fibworker.domain.fib.FibonacciStrategyFactory;
import org.gol.fibworker.domain.model.JobId;
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
import java.nio.file.Path;
import java.util.UUID;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

import static java.nio.file.Files.readString;
import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.commons.function.Try.call;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobMessageAdapterTest {

    private static final Path CONTRACT_DIR = Path.of("src/test/resources/contract-broker");
    private static final TaskId MESSAGE_TASK_ID = new TaskId(UUID.fromString("5753a351-fc13-4d10-8fd5-28bd15124501"));
    private static final JobId MESSAGE_JOB_ID = new JobId(UUID.fromString("84f423f4-b5d4-41ed-9ff9-d591b64cd25b"));
    private static final String MESSAGE_ALGORITHM = "ITERATIVE";
    private static final int MESSAGE_NUMBER = 45;
    private static final FibonacciResult FIB_RESULT = new FibonacciResult(BigInteger.valueOf(1134903170), ofMillis(100));
    private static final String ERROR_MESSAGE = "calc error";

    @Mock
    private ResultPort resultPort;
    @Mock
    private FibonacciStrategyFactory strategyFactory;
    @Mock
    private TextMessage message;
    @Captor
    private ArgumentCaptor<SuccessResultCmd> successCmdCaptor;
    @Captor
    private ArgumentCaptor<FailureResultCmd> failureCmdCapture;
    private final FibonacciStrategy str = () -> FIB_RESULT;

    private JobMessageAdapter sut;

    @BeforeEach
    void init() {
        sut = new JobMessageAdapter(resultPort, strategyFactory);
    }

    @Test
    @DisplayName("should correctly process valid message [positive]")
    void shouldProcessCorrectMessage() throws JMSException {
        //given
        doReturn(getMessagePayload("message-valid.json")).when(message).getText();
        doReturn(str).when(strategyFactory).findStrategy(MESSAGE_ALGORITHM, MESSAGE_NUMBER);

        //when
        sut.handleFibonacciJob(message);

        //then
        verify(resultPort, never()).sendResult(isA(FailureResultCmd.class));
        verify(resultPort).sendResult(successCmdCaptor.capture());
        var cmd = successCmdCaptor.getValue();
        assertThat(cmd.taskId())
                .isEqualTo(MESSAGE_TASK_ID);
        assertThat(cmd.jobId())
                .isEqualTo(MESSAGE_JOB_ID);
        assertThat(cmd.result())
                .isEqualTo(FIB_RESULT);
    }

    @Test
    @DisplayName("should handle silently corrupted message [negative]")
    void shouldHandleCorruptedMessage() throws JMSException {
        //given
        doReturn(getMessagePayload("message-corrupted.json")).when(message).getText();

        //when
        sut.handleFibonacciJob(message);

        //then
        verify(resultPort, never()).sendResult(isA(SuccessResultCmd.class));
        verify(resultPort, never()).sendResult(isA(FailureResultCmd.class));
    }

    @Test
    @DisplayName("should handle process failure [negative]")
    void shouldHandleProcessingError() throws JMSException {
        //given
        doReturn(getMessagePayload("message-valid.json")).when(message).getText();
        doThrow(new IllegalStateException(ERROR_MESSAGE)).when(strategyFactory).findStrategy(MESSAGE_ALGORITHM, MESSAGE_NUMBER);

        //when
        sut.handleFibonacciJob(message);

        //then
        verify(resultPort, never()).sendResult(isA(SuccessResultCmd.class));
        verify(resultPort).sendResult(failureCmdCapture.capture());
        var cmd = failureCmdCapture.getValue();
        assertThat(cmd.taskId())
                .isEqualTo(MESSAGE_TASK_ID);
        assertThat(cmd.jobId())
                .isEqualTo(MESSAGE_JOB_ID);
        assertThat(cmd.errorMessage().value())
                .isEqualTo(ERROR_MESSAGE);
    }

    private String getMessagePayload(String filename) {
        return call(() -> readString(CONTRACT_DIR.resolve(filename)))
                .getOrThrow(IllegalArgumentException::new);
    }
}