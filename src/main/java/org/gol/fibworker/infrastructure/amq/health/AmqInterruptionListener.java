package org.gol.fibworker.infrastructure.amq.health;

import org.apache.activemq.transport.DefaultTransportListener;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
class AmqInterruptionListener extends DefaultTransportListener {

    private boolean interrupted;

    @Override
    public void transportInterupted() {
        log.warn("AMQ transport has been interrupted");
        interrupted = true;
    }

    @Override
    public void transportResumed() {
        log.info("AMQ transport has been resumed");
        interrupted = false;
    }
}
