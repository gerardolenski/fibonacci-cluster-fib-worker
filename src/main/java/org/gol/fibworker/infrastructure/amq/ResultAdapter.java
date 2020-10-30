package org.gol.fibworker.infrastructure.amq;

import org.gol.fibworker.domain.result.ResultData;
import org.gol.fibworker.domain.result.ResultPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResultAdapter implements ResultPort {
    @Override
    public void sendResult(UUID taskId, ResultData resultData) {
        //TODO implement me
    }
}
