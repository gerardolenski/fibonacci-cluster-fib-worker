package org.gol.fibworker.application;

import org.gol.fibworker.BaseIT;
import org.gol.fibworker.domain.fib.FibonacciProcessingPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.readString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * An integration test with Artemis on Testcontainers.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "mq.worker.queue-name=JobMessageAdapterIntTest")
class JobMessageAdapterIntTest extends BaseIT {

    private static final int MAX_TIMEOUT = 10000;
    private static final Path MESSGAE_CONTRACT_PATH = Path.of("src/test/resources/contract-broker/message-valid.json");

    @Autowired
    private JmsTemplate jmsTemplate;
    @MockBean
    private FibonacciProcessingPort processingPort;

    @Test
    void shouldReceiveMessage() throws IOException {
        //given
        var messagePayload = readString(MESSGAE_CONTRACT_PATH);

        //when
        jmsTemplate.convertAndSend("JobMessageAdapterIntTest",
                messagePayload,
                message -> {
                    message.setStringProperty("worker", "FIBONACCI");
                    return message;
                });

        //then
        verify(processingPort, timeout(MAX_TIMEOUT)).calcFibonacci(any());
    }
}
