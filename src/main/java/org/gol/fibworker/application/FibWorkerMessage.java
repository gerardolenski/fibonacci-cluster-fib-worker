package org.gol.fibworker.application;

import java.util.UUID;

record FibWorkerMessage(UUID taskId, UUID jobId, String data) {
}
