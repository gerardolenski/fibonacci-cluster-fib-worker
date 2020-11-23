package org.gol.fibworker.infrastructure.amq.health;

import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AmgFailoverHealthCheckConfig {

    @Bean
    AmqInterruptionListener amqInterruptionListener() {
        return new AmqInterruptionListener();
    }

    @Bean
    ActiveMQConnectionFactoryCustomizer amqConnectionFactoryCustomizer(
            AmqInterruptionListener interruptionListener) {
        return factory -> factory.setTransportListener(interruptionListener);
    }

    @Bean("amqFailover")
    AmqFailoverHealthCheck amqFailoverHealthCheck(
            AmqInterruptionListener interruptionListener) {
        return new AmqFailoverHealthCheck(interruptionListener);
    }
}
