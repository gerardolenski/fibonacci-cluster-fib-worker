package org.gol.fibworker.infrastructure.amq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.gol.fibworker.domain.result.ResultData;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.util.UUID;
import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
class AmqResultAdapter implements ResultPort {

    public static final String RESULT_PROPERTY_NAME = "workerResult";

    private static final BiFunction<UUID, ResultData, FibResultMessage> TO_MESSAGE = (taskId, data) ->
            FibResultMessage.builder()
                    .taskId(taskId)
                    .jobId(data.getJobId())
                    .result(data.getResult())
                    .processingTime(data.getProcessingTime())
                    .errorMessage(data.getErrorMessage())
                    .build();

    private static final MessagePostProcessor RESULT_POSTPROCESSOR = message -> {
        message.setBooleanProperty(RESULT_PROPERTY_NAME, true);
        return message;
    };

    private final JmsTemplate jmsTemplate;
    private final String queueName;

    @Override
    public void sendResult(UUID taskId, ResultData resultData) {
        var message = TO_MESSAGE.apply(taskId, resultData);
        log.trace("Sending result message: {}", message);
        jmsTemplate.convertAndSend(
                new ActiveMQQueue(queueName),
                message,
                RESULT_POSTPROCESSOR);
    }
}
