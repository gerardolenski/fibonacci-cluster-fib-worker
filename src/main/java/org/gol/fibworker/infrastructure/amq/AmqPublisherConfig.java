package org.gol.fibworker.infrastructure.amq;

import lombok.extern.slf4j.Slf4j;
import org.gol.fibworker.domain.config.params.ConfigurationPort;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;

@Slf4j
@Configuration
class AmqPublisherConfig {

    @Bean
    JmsTemplate jmsTemplate(
            @Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
            @Qualifier("jmsMessageConverter") MessageConverter jmsMessageConverter) {
        var jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(jmsMessageConverter);
        return jmsTemplate;
    }

    @Bean
    ResultPort amqResultAdapter(JmsTemplate jmsTemplate, ConfigurationPort config) {
        return new AmqResultAdapter(jmsTemplate, config.getWorkerQueueName());
    }
}
