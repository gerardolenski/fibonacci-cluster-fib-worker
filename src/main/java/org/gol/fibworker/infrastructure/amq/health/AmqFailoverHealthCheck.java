package org.gol.fibworker.infrastructure.amq.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.boot.actuate.health.Health.outOfService;
import static org.springframework.boot.actuate.health.Health.up;

@Slf4j
@RequiredArgsConstructor
class AmqFailoverHealthCheck implements HealthIndicator {

    private final AmqInterruptionListener amqInterruptionListener;

    @Override
    public Health health() {
        if(amqInterruptionListener.isInterrupted()) {
            log.error("The AMQ failover connection is inactive.");
            return outOfService().build();
        }
        return up().build();
    }
}
