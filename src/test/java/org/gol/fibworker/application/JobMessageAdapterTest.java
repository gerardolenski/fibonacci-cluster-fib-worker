package org.gol.fibworker.application;

import org.gol.fibworker.domain.FibonacciProcessingCmd;
import org.gol.fibworker.domain.FibonacciProcessingPort;
import org.gol.fibworker.domain.model.AlgorithmClaim;
import org.gol.fibworker.domain.model.JobId;
import org.gol.fibworker.domain.model.SequenceBase;
import org.gol.fibworker.domain.model.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.UUID;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

import static java.nio.file.Files.readString;
import static org.junit.platform.commons.function.Try.call;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobMessageAdapterTest {

    private static final Path CONTRACT_DIR = Path.of("src/test/resources/contract-broker");
    private static final TaskId MESSAGE_TASK_ID = new TaskId(UUID.fromString("5753a351-fc13-4d10-8fd5-28bd15124501"));
    private static final JobId MESSAGE_JOB_ID = new JobId(UUID.fromString("84f423f4-b5d4-41ed-9ff9-d591b64cd25b"));
    private static final AlgorithmClaim MESSAGE_ALGORITHM = new AlgorithmClaim("ITERATIVE");
    private static final SequenceBase MESSAGE_NUMBER = new SequenceBase(45);

    @Mock
    private FibonacciProcessingPort processingPort;
    @Mock
    private TextMessage message;
    private JobMessageAdapter sut;

    @BeforeEach
    void init() {
        sut = new JobMessageAdapter(processingPort);
        sut = new JobMessageAdapter(processingPort);
    }

    @Test
    @DisplayName("should correctly process valid message [positive]")
    void shouldProcessCorrectMessage() throws JMSException {
        //given
        doReturn(getMessagePayload("message-valid.json")).when(message).getText();

        //when
        sut.handleFibonacciJob(message);

        //then
        verify(processingPort).calcFibonacci(FibonacciProcessingCmd.builder()
                .taskId(MESSAGE_TASK_ID)
                .jobId(MESSAGE_JOB_ID)
                .algorithmClaim(MESSAGE_ALGORITHM)
                .sequenceBase(MESSAGE_NUMBER)
                .build());
    }

    @Test
    @DisplayName("should handle silently corrupted message [negative]")
    void shouldHandleCorruptedMessage() throws JMSException {
        //given
        doReturn(getMessagePayload("message-corrupted.json")).when(message).getText();

        //when
        sut.handleFibonacciJob(message);

        //then
        verify(processingPort, never()).calcFibonacci(any());
    }

    private String getMessagePayload(String filename) {
        return call(() -> readString(CONTRACT_DIR.resolve(filename)))
                .getOrThrow(IllegalArgumentException::new);
    }
}