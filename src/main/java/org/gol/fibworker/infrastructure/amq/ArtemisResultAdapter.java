package org.gol.fibworker.infrastructure.amq;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.gol.fibworker.domain.result.FailureResultCmd;
import org.gol.fibworker.domain.result.ResultPort;
import org.gol.fibworker.domain.result.SuccessResultCmd;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class ArtemisResultAdapter implements ResultPort {

    public static final String RESULT_PROPERTY_NAME = "workerResult";

    private static final MessagePostProcessor RESULT_POSTPROCESSOR = message -> {
        message.setBooleanProperty(RESULT_PROPERTY_NAME, true);
        return message;
    };

    private final JmsTemplate jmsTemplate;
    private final String queueName;

    @Override
    public void sendResult(SuccessResultCmd cmd) {
        var message = FibResultMessage.builder()
                .taskId(cmd.taskId().value())
                .jobId(cmd.jobId().value())
                .result(cmd.result().number())
                .processingTime(cmd.result().executionTime().toMillis())
                .build();
        log.debug("Sending success result message: {}", message);
        sendToBroker(message);
    }

    @Override
    public void sendResult(FailureResultCmd cmd) {
        var message = FibResultMessage.builder()
                .taskId(cmd.taskId().value())
                .jobId(cmd.jobId().value())
                .errorMessage(cmd.errorMessage().value())
                .build();
        log.debug("Sending failure result message: {}", message);
        sendToBroker(message);
    }

    private void sendToBroker(FibResultMessage message) {
        jmsTemplate.convertAndSend(
                new ActiveMQQueue(queueName),
                message,
                RESULT_POSTPROCESSOR);
    }
}
