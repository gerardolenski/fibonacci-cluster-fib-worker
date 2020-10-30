package org.gol.fibworker.domain.result;

import java.util.UUID;

public interface ResultPort {
    void sendResult(UUID taskId, ResultData resultData);
}
