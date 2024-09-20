package org.gol.fibworker.domain.result;

/**
 * Secondary (driven) port sending the result of the Fibonacci number calculation.
 */
public interface ResultPort {

    /**
     * Sends the success calculation.
     */
    void sendResult(SuccessResultCmd cmd);

    /**
     * Sends the failure calculation.
     */
    void sendResult(FailureResultCmd cmd);
}
