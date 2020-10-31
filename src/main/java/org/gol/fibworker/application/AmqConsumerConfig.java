package org.gol.fibworker.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gol.fibworker.domain.config.params.ConfigurationPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static org.springframework.jms.support.converter.MessageType.TEXT;

@Slf4j
@Configuration
@RequiredArgsConstructor
class AmqConsumerConfig {

    static final String WORKER_QUEUE_FACTORY = "workerQueue";
    private static final String TYPE_ID_PROPERTY_NAME = "_type";

    private final ConfigurationPort configurationPort;

    @Bean(WORKER_QUEUE_FACTORY)
    JmsListenerContainerFactory<DefaultMessageListenerContainer> queueListenerFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory) {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(initJmsMessageConverter());
        factory.setErrorHandler(e -> log.error("Message processing error: {}", e.getMessage(), e));
        return factory;
    }

    private MessageConverter initJmsMessageConverter() {
        var converter = new MappingJackson2MessageConverter();
        converter.setTargetType(TEXT);
        converter.setTypeIdPropertyName(TYPE_ID_PROPERTY_NAME);
        converter.setTypeIdMappings(getMessageTypeIdMapping());
        converter.setObjectMapper(initJmsObjectMapper());
        return converter;
    }

    private ObjectMapper initJmsObjectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule(PROPERTIES));
    }

    private Map<String, Class<?>> getMessageTypeIdMapping() {
        return Map.of(configurationPort.getWorkerMessageTypeId(), FibWorkerMessage.class);
    }
}
