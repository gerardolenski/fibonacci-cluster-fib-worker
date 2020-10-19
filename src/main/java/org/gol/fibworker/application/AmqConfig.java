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
class AmqConfig {

    static final String WORKER_QUEUE_FACTORY = "workerQueue";
    private static final String TYPE_ID_PROPERTY_NAME = "_type";

    private final ConfigurationPort configurationPort;

    @Bean
    ObjectMapper jmsObjectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule(PROPERTIES));
    }

    @Bean
    MessageConverter jmsMessageConverter(@Qualifier("jmsObjectMapper") ObjectMapper mapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(TEXT);
        converter.setTypeIdPropertyName(TYPE_ID_PROPERTY_NAME);
        converter.setTypeIdMappings(getMessageTypeIdMapping());
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean(WORKER_QUEUE_FACTORY)
    public JmsListenerContainerFactory<DefaultMessageListenerContainer> queueListenerFactory(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            @Qualifier("jmsMessageConverter") MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(messageConverter);
        factory.setErrorHandler(e -> log.error("Cannot consume message: {}", e.getMessage(), e));
        return factory;
    }

    private Map<String, Class<?>> getMessageTypeIdMapping() {
        return Map.of(configurationPort.getWorkerMessageTypeId(), FibWorkerMessage.class);
    }
}
